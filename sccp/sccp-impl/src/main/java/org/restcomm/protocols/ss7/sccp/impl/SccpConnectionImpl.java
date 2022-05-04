/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.restcomm.protocols.ss7.sccp.impl;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnectionImpl extends SccpConnectionWithCouplingImpl implements SccpConnection {

    public SccpConnectionImpl(int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(stack.newSls(), localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    public void receiveMessage(SccpConnMessage message) throws Exception {
    	super.receiveMessage(message);
    }

    protected void sendMessage(SccpConnMessage message) throws Exception {
    	super.sendMessage(message);
    }

    protected void callListenerOnData(ByteBuf data) {
        SccpListener listener = getListener();
        if (listener != null) { // when listener is absent it's handled by SccpRoutingControl
            listener.onData(this, data);
        }
    }

    public void prepareMessageForSending(SccpConnSegmentableMessageImpl message) {
        // not needed for protocol class 2
    }

    protected void prepareMessageForSending(SccpConnItMessageImpl message) {
        // not needed for protocol class 2
    }

    public static class ConnectionNotAvailableException extends IllegalStateException {
		private static final long serialVersionUID = 1L;

		public ConnectionNotAvailableException(String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SccpConnection[");

        fillSccpConnectionFields(sb);
        sb.append("]");

        return sb.toString();
    }

    protected void fillSccpConnectionFields(StringBuilder sb) {
        LocalReference lr = getLocalReference();
        LocalReference rr = getRemoteReference();
        ProtocolClass pClass = getProtocolClass();

        sb.append("localReference=");
        if (lr != null)
            sb.append(lr.getValue());
        else
            sb.append("null");
        sb.append(", remoteReference=");
        if (rr != null)
            sb.append(rr.getValue());
        else
            sb.append("null");
        sb.append(", localSsn=");
        sb.append(getLocalSsn());
        sb.append(", remoteSsn=");
        sb.append(remoteSsn);
        sb.append(", remoteDpc=");
        sb.append(remoteDpc);
        sb.append(", state=");
        sb.append(getState());
        sb.append(", protocolClass=");
        if (pClass != null)
            sb.append(getProtocolClass().getProtocolClass());
        else
            sb.append("null");
        if (isAwaitSegments())
            sb.append(", awaitSegments");
        if (isCouplingEnabled())
            sb.append(", couplingEnabled");
    }
}
