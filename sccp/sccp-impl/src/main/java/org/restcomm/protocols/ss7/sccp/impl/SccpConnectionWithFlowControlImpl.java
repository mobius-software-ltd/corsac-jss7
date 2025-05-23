/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.sccp.impl;

import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CR_RECEIVED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED_SEND_WINDOW_EXHAUSTED;

import java.util.concurrent.atomic.AtomicBoolean;

import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnAkMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import com.mobius.software.common.dal.timers.TaskCallback;

import io.netty.buffer.ByteBuf;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnectionWithFlowControlImpl extends SccpConnectionImpl implements SccpConnection {

    protected SccpFlowControl flow;

    private AtomicBoolean overloaded=new AtomicBoolean(false);

    public SccpConnectionWithFlowControlImpl(int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(localSsn, localReference, protocol, stack, sccpRoutingControl);
        if (protocol.getProtocolClass() != 3) {
            logger.error("Using connection class for non-supported protocol class 2");
            throw new IllegalArgumentException();
        }
    }

    @Override
	public void establish(SccpConnCrMessage message, TaskCallback<Exception> callback) {
        this.flow = newSccpFlowControl(message.getCredit());
        super.establish(message, callback);
    }

    @Override
	public void confirm(SccpAddress respondingAddress, Credit credit, ByteBuf data, TaskCallback<Exception> callback) throws Exception {
        if (getState() != CR_RECEIVED) {
            logger.error(String.format("Trying to confirm connection in non-compatible state %s", getState()));
            throw new IllegalStateException(String.format("Trying to confirm connection in non-compatible state %s", getState()));
        }
        this.flow = newSccpFlowControl(credit);

        super.confirm(respondingAddress, credit, data, callback);
    }

    protected SccpFlowControl newSccpFlowControl(Credit credit) {
        return new SccpFlowControl(stack.name, credit.getValue());
    }

    public void setOverloaded(boolean overloaded) throws Exception {
    	if(!this.overloaded.compareAndSet(!overloaded, overloaded))
    	    return;
        
        if (overloaded)
			sendAk(new CreditImpl(0));
		else
			sendAk();    
    }

    @Override
	public void prepareMessageForSending(SccpConnSegmentableMessageImpl message) {
        if (message instanceof SccpConnDt2MessageImpl) {
            SccpConnDt2MessageImpl dt2 = (SccpConnDt2MessageImpl) message;

            flow.initializeMessageNumbering(dt2);
            flow.checkOutputMessageNumbering(dt2);

            if (!flow.isAuthorizedToTransmitAnotherMessage())
				setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);

        } else
			throw new IllegalArgumentException();
    }

    @Override
	protected void prepareMessageForSending(SccpConnItMessageImpl it) {
        it.setCredit(new CreditImpl(flow.getReceiveCredit()));

        flow.initializeMessageNumbering(it);
        flow.checkOutputMessageNumbering(it);

        if (!flow.isAuthorizedToTransmitAnotherMessage())
			setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
    }

    @Override
	public void receiveMessage(SccpConnMessage message) throws Exception {
    	super.receiveMessage(message);

        if (message instanceof SccpConnCcMessageImpl) {
            SccpConnCcMessageImpl cc = (SccpConnCcMessageImpl) message;
            if (cc.getCredit() != null)
				this.flow = newSccpFlowControl(cc.getCredit());

        } else if (message instanceof SccpConnAkMessageImpl)
			handleAkMessage((SccpConnAkMessageImpl) message);
		else if (message instanceof SccpConnRsrMessageImpl)
			flow.reinitialize();
    }

    @Override
	protected void receiveDataMessage(SccpConnSegmentableMessageImpl msg) throws Exception {
        if (!isAvailable()) {
            logger.error(getState() + " Message discarded " + msg);
            return;
        }
        if (!(msg instanceof SccpConnDt2MessageImpl)) {
            logger.error("Using protocol class 3, DT1 message discarded " + msg);
            return;
        }

        SccpConnDt2MessageImpl dt2 = (SccpConnDt2MessageImpl) msg;

        boolean correctNumbering = flow.checkInputMessageNumbering(this, dt2.getSequencingSegmenting().getSendSequenceNumber(),
                dt2.getSequencingSegmenting().getReceiveSequenceNumber());

        if (flow.isAkSendCriterion(dt2))
			sendAk();
        if (correctNumbering)
			super.receiveDataMessage(msg);
		else
			logger.error(String.format("Message %s was discarded due to incorrect sequence numbers", msg.toString()));

        if (flow.isAuthorizedToTransmitAnotherMessage())
			setState(ESTABLISHED);
		else
			setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
    }

    protected void sendAk() throws Exception {
        sendAk(new CreditImpl(flow.getMaximumWindowSize()));
    }

    protected void sendAk(Credit credit) throws Exception {
        SccpConnAkMessageImpl msg = new SccpConnAkMessageImpl(0, 0);
        msg.setDestinationLocalReferenceNumber(getRemoteReference());
        msg.setSourceLocalReferenceNumber(getLocalReference());
        msg.setCredit(credit);

        flow.setReceiveCredit(credit.getValue());
        flow.initializeMessageNumbering(msg);

        sendMessage(msg, dummyCallback);
    }

    private void handleAkMessage(SccpConnAkMessageImpl msg) throws Exception {


        flow.checkInputMessageNumbering(this, msg.getReceiveSequenceNumber().getNumber());
        flow.setSendCredit(msg.getCredit().getValue());

        if (flow.isAuthorizedToTransmitAnotherMessage())
			setState(ESTABLISHED);
		else
			setState(ESTABLISHED_SEND_WINDOW_EXHAUSTED);
    }

    @Override
	public void reset(ResetCause reason, TaskCallback<Exception> callback) throws Exception {
        super.reset(reason, callback);
        flow.reinitialize();
    }

    @Override
	protected boolean isCanSendData() {
    	SccpConnectionState oldState = getState();
        if (oldState == ESTABLISHED_SEND_WINDOW_EXHAUSTED && flow.isAuthorizedToTransmitAnotherMessage())
			setState(ESTABLISHED);
        return getState() == ESTABLISHED;
    }

    @Override
	public Credit getSendCredit() {
        return new CreditImpl(flow.getSendCredit());
    }

    @Override
	public Credit getReceiveCredit() {
        return new CreditImpl(flow.getReceiveCredit());
    }

    protected boolean isPreemptiveAck() {
        return flow.isPreemptiveAk();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ConnectionWithFlowControl[");

        fillSccpConnectionFields(sb);
        if (overloaded.get())
            sb.append(", overloaded");
        sb.append("]");

        return sb.toString();
    }
}
