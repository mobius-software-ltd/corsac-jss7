package org.restcomm.protocols.ss7.sccp.impl;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;

abstract class SccpConnectionWithTransmitQueueImpl extends SccpConnectionBaseImpl {
    
    public SccpConnectionWithTransmitQueueImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    protected void sendMessage(SccpConnMessage message) throws Exception {
        if (stack.state != SccpStackImpl.State.RUNNING) {
            logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
            return;
        }
        if (!(message instanceof SccpConnSegmentableMessageImpl)) {
            super.sendMessage(message);

        } else {
            if (MessageUtil.getDln(message) == null) {
                logger.error(String.format("Message doesn't have DLN set: ", message));
                throw new IllegalStateException();
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug("Polling another message from queue: " + message.toString());
            }

            try {
                SccpConnectionWithTransmitQueueImpl.super.sendMessage(message);

            } catch (Exception e) {
                // log here Exceptions from MTP3 level
                logger.error("IOException when sending the message: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public void reset(ResetCause reason) throws Exception {
        super.reset(reason);
    }

    public void disconnect(ReleaseCause reason, ByteBuf data) throws Exception {
        super.disconnect(reason, data);
    }

    public void refuse(RefusalCause reason, ByteBuf data) throws Exception {
        super.refuse(reason, data);
    }
}
