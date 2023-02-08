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

import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrefMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt1MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnErrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;

import java.io.IOException;

import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_PROPAGATED_VIA_COUPLED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED_WILL_PROPAGATE;

/*
 * Is inherited by both protocol class 2 and 3 implementations. Skips execution when connection isn't coupled
 */
/**
 * 
 * @author yulianoifa
 *
 */
public abstract class SccpConnectionWithCouplingImpl extends SccpConnectionWithSegmentingImpl {
    protected SccpConnectionWithCouplingImpl nextConn;

    private boolean couplingEnabled;

    public SccpConnectionWithCouplingImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        super.receiveMessage(message);
        if (couplingEnabled) {
            if (message instanceof SccpConnCcMessageImpl) {
                SccpConnCcMessageImpl cc = (SccpConnCcMessageImpl) message;
                nextConn.confirm(cc.getCalledPartyAddress(), cc.getCredit(), cc.getUserData());

            } else if (message instanceof SccpConnCrefMessageImpl) {
                SccpConnCrefMessageImpl cref = (SccpConnCrefMessageImpl) message;

                SccpConnCrefMessageImpl copy = new SccpConnCrefMessageImpl(nextConn.getSls(), nextConn.getLocalSsn());
                copy.setSourceLocalReferenceNumber(nextConn.getLocalReference());
                copy.setDestinationLocalReferenceNumber(nextConn.getRemoteReference());
                copy.setOutgoingDpc(nextConn.getRemoteDpc());
                copy.setUserData(cref.getUserData());
                copy.setImportance(cref.getImportance());
                copy.setRefusalCause(cref.getRefusalCause());
                copy.setCalledPartyAddress(cref.getCalledPartyAddress());

                stack.removeConnection(getLocalReference());
                nextConn.sendMessage(copy);
                stack.removeConnection(nextConn.getLocalReference());

            } else if (message instanceof SccpConnRlsdMessageImpl) {
                SccpConnRlsdMessageImpl rlsd = (SccpConnRlsdMessageImpl) message;
                nextConn.disconnect(rlsd.getReleaseCause(), rlsd.getUserData());

            } else if (message instanceof SccpConnRlcMessageImpl) {
                SccpConnRlcMessageImpl copy = new SccpConnRlcMessageImpl(nextConn.getSls(), nextConn.getLocalSsn());
                copy.setSourceLocalReferenceNumber(nextConn.getLocalReference());
                copy.setDestinationLocalReferenceNumber(nextConn.getRemoteReference());
                copy.setOutgoingDpc(nextConn.getRemoteDpc());

                stack.removeConnection(getLocalReference());
                nextConn.sendMessage(copy);
                stack.removeConnection(nextConn.getLocalReference());
            } else if (message instanceof SccpConnRsrMessageImpl) {
                SccpConnRsrMessageImpl rsr = (SccpConnRsrMessageImpl) message;
                setState(RSR_RECEIVED_WILL_PROPAGATE);
                nextConn.reset(rsr.getResetCause());
                setState(RSR_PROPAGATED_VIA_COUPLED);

            } else if (message instanceof SccpConnRscMessageImpl) {
                SccpConnRscMessageImpl copy = new SccpConnRscMessageImpl(nextConn.getSls(), nextConn.getLocalSsn());
                copy.setSourceLocalReferenceNumber(nextConn.getLocalReference());
                copy.setDestinationLocalReferenceNumber(nextConn.getRemoteReference());
                copy.setOutgoingDpc(nextConn.getRemoteDpc());

                nextConn.sendMessage(copy);
                setState(ESTABLISHED);
                nextConn.setState(ESTABLISHED);
            } else if (message instanceof SccpConnErrMessageImpl) {
                nextConn.sendErr(((SccpConnErrMessageImpl) message).getErrorCause());
            }
        }
    }

    protected void receiveDataMessage(SccpConnSegmentableMessageImpl msg) throws Exception {
        if (couplingEnabled) {
            if (msg instanceof SccpConnDt1MessageImpl) {
                SccpConnDt1MessageImpl copy = new SccpConnDt1MessageImpl(255, nextConn.getSls(), nextConn.getLocalSsn());
                copy.setSegmentingReassembling(((SccpConnDt1MessageImpl) msg).getSegmentingReassembling());
                copy.setSourceLocalReferenceNumber(nextConn.getLocalReference());
                copy.setDestinationLocalReferenceNumber(nextConn.getRemoteReference());
                copy.setOutgoingDpc(nextConn.getRemoteDpc());
                copy.setUserData(msg.getUserData());

                nextConn.sendMessage(copy);

            } else if (msg instanceof SccpConnDt2MessageImpl) {
                SccpConnDt2MessageImpl copy = new SccpConnDt2MessageImpl(255, nextConn.getSls(), nextConn.getLocalSsn());
                copy.setSequencingSegmenting(((SccpConnDt2MessageImpl) msg).getSequencingSegmenting());
                copy.setSourceLocalReferenceNumber(nextConn.getLocalReference());
                copy.setDestinationLocalReferenceNumber(nextConn.getRemoteReference());
                copy.setOutgoingDpc(nextConn.getRemoteDpc());
                copy.setUserData(msg.getUserData());

                nextConn.sendMessage(copy);
            }
        } else {
            super.receiveDataMessage(msg);
        }
    }

    protected void confirmRelease() throws Exception {
        if (!couplingEnabled) {
            super.confirmRelease();
        }
    }

    protected void confirmReset() throws Exception {
        if (!couplingEnabled) {
            super.confirmReset();
        }
    }

    protected void checkLocalListener() throws IOException {
        if (!couplingEnabled) {
            super.checkLocalListener();
        }
    }

    public void enableCoupling(SccpConnectionWithCouplingImpl nextConn) {
        if (nextConn == null || getState() != SccpConnectionState.NEW || nextConn.getState() != SccpConnectionState.NEW) {
            throw new IllegalArgumentException();
        }
        this.couplingEnabled = true;
        this.nextConn = nextConn;
        if (!nextConn.couplingEnabled && nextConn.nextConn != this) {
            nextConn.enableCoupling(this);
        }

    }

    public boolean isCouplingEnabled() {
        return couplingEnabled;
    }
}
