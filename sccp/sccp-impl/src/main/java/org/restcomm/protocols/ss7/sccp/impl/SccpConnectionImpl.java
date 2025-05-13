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

import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
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
public class SccpConnectionImpl extends SccpConnectionWithCouplingImpl implements SccpConnection {

    public SccpConnectionImpl(int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(stack.newSls(), localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    @Override
	public void receiveMessage(SccpConnMessage message) throws Exception {
    	super.receiveMessage(message);
    }

    @Override
	public void sendMessage(SccpConnMessage message, TaskCallback<Exception> callback) {
    	super.sendMessage(message, callback);
    }

    @Override
	protected void callListenerOnData(ByteBuf data) {
        SccpListener listener = getListener();
        if (listener != null)
			listener.onData(this, data);
    }

    @Override
	public void prepareMessageForSending(SccpConnSegmentableMessageImpl message) {
        // not needed for protocol class 2
    }

    @Override
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
