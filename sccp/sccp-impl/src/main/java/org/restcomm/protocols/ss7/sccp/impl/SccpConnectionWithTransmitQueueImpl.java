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

import org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;

import com.mobius.software.common.dal.timers.TaskCallback;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @author yulianoifa
 *
 */
abstract class SccpConnectionWithTransmitQueueImpl extends SccpConnectionBaseImpl {

	public SccpConnectionWithTransmitQueueImpl(int sls, int localSsn, LocalReference localReference,
			ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
		super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
	}

	@Override
	public void sendMessage(SccpConnMessage message, TaskCallback<Exception> callback) {
		if (stack.state != SccpStackImpl.State.RUNNING) {
			String errorMessage = "Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING";

			logger.error(errorMessage);
			callback.onError(new IllegalStateException(errorMessage));
			return;
		}
		if (!(message instanceof SccpConnSegmentableMessageImpl))
			super.sendMessage(message, callback);
		else {
			if (MessageUtil.getDln(message) == null) {
				String errorMessage = String.format("Message doesn't have DLN set: ", message);

				logger.error(errorMessage);
				callback.onError(new IllegalArgumentException(errorMessage));
				return;
			}

			if (logger.isDebugEnabled())
				logger.debug("Polling another message from queue: " + message.toString());

			SccpConnectionWithTransmitQueueImpl.super.sendMessage(message, callback);
		}
	}

	@Override
	public void reset(ResetCause reason, TaskCallback<Exception> callback) throws Exception {
		super.reset(reason, callback);
	}

	@Override
	public void disconnect(ReleaseCause reason, ByteBuf data, TaskCallback<Exception> callback) {
		super.disconnect(reason, data, callback);
	}

	@Override
	public void refuse(RefusalCause reason, ByteBuf data, TaskCallback<Exception> callback) throws Exception {
		super.refuse(reason, data, callback);
	}
}
