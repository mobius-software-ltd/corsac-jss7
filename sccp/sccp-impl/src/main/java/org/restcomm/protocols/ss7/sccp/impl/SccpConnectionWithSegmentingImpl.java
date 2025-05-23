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

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt1MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;

import com.mobius.software.common.dal.timers.TaskCallback;

import io.netty.buffer.ByteBuf;
/**
 * 
 * @author yulianoifa
 *
 */
abstract class SccpConnectionWithSegmentingImpl extends SccpConnectionWithTimers {
    private boolean awaitSegments = false;
    private SccpConnSegmentableMessageImpl currentSegmentedMessage;

    public SccpConnectionWithSegmentingImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack,
                                            SccpRoutingControl sccpRoutingControl) {
        super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    public boolean isAwaitSegments() {
        return awaitSegments;
    }

    @Override
	protected void receiveMessage(SccpConnMessage message) throws Exception {
        super.receiveMessage(message);

        if (message instanceof SccpConnSegmentableMessageImpl)
			receiveDataMessage((SccpConnSegmentableMessageImpl) message);
    }

    protected void receiveDataMessage(SccpConnSegmentableMessageImpl msg) throws Exception {
        if (!msg.isMoreData() && !awaitSegments)
			callListenerOnData(msg.getUserData());
		else if (msg.isMoreData()) {
            awaitSegments = true;
            if (currentSegmentedMessage == null)
				currentSegmentedMessage = msg;
			else
				currentSegmentedMessage.setReceivedNextSegment(msg);
        } else if (!msg.isMoreData() && awaitSegments) {
            currentSegmentedMessage.setReceivedNextSegment(msg);
            awaitSegments = false;

            if (!currentSegmentedMessage.isFullyRecieved()) {
                logger.error(String.format("Message is expected to be fully received but it isn't: %s", msg));
                throw new IllegalStateException();
            }

            callListenerOnData(currentSegmentedMessage.getUserData());
            currentSegmentedMessage = null;
        }
    }

    public void send(ByteBuf data, TaskCallback<Exception> callback) throws Exception {

        if (data.readableBytes() <= 255)
			sendDataMessageSegment(data, false);
		else {
            int chunks = (int) Math.ceil(data.readableBytes() / 255.0);
            int pos = 0;
            List<ByteBuf> chunkData = new ArrayList<>();
            for (int i = 0; i < chunks; i++) {
                int copyBytes;
                if (i != chunks - 1) {
                    copyBytes = 255;                    
                    chunkData.add(data.slice(data.readerIndex() + pos, copyBytes));
                } else {
                    copyBytes = data.readableBytes() - i * 255;
                    chunkData.add(data.slice(data.readerIndex() + pos, copyBytes));
                }

                pos += copyBytes;
            }
            for (int i = 0; i < chunkData.size(); i++)
				sendDataMessageSegment(chunkData.get(i), i != chunkData.size() - 1);
        }
        
        callback.onSuccess();
    }

    private void sendDataMessageSegment(ByteBuf data, boolean moreData) throws Exception {
        if (data.readableBytes() > 255) {
            logger.error("Message data is too lengthy");
            throw new IllegalArgumentException("Message data is too lengthy");
        }
        if (logger.isDebugEnabled())
			logger.debug(String.format("Sending data message to DPC=%d, SSN=%d, DLR=%s", getRemoteDpc(),
                    getRemoteSsn(), getRemoteReference()));
        if (!isAvailable())
			throw new SccpConnectionImpl.ConnectionNotAvailableException(String.format("Trying to send data when in non-compatible state %s", getState()));

        SccpConnSegmentableMessageImpl dataMessage;

        if (getProtocolClass().getProtocolClass() == 2)
			dataMessage = new SccpConnDt1MessageImpl(255, getSls(), getLocalSsn());
		else
			dataMessage = new SccpConnDt2MessageImpl(255, getSls(), getLocalSsn());

        dataMessage.setDestinationLocalReferenceNumber(getRemoteReference());
        dataMessage.setSourceLocalReferenceNumber(getLocalReference());
        dataMessage.setUserData(data);
        dataMessage.setMoreData(moreData);
        lastMoreDataSent = moreData;

        if (MessageUtil.getDln(dataMessage) == null) {
            logger.error(String.format("Message doesn't have DLN set: ", dataMessage));
            throw new IllegalStateException();
        }
        sendMessage(dataMessage, dummyCallback);
    }
}
