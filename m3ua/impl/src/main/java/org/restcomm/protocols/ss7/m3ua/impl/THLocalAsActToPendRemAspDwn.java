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

package org.restcomm.protocols.ss7.m3ua.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.restcomm.protocols.ss7.m3ua.message.MessageClass;
import org.restcomm.protocols.ss7.m3ua.message.MessageType;
import org.restcomm.protocols.ss7.m3ua.message.mgmt.Notify;
import org.restcomm.protocols.ss7.m3ua.parameter.Status;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class THLocalAsActToPendRemAspDwn implements TransitionHandler {

    private static final Logger logger = LogManager.getLogger(THLocalAsActToPendRemAspDwn.class);

    private AsImpl asImpl = null;
    private FSM fsm;

    private int lbCount = 0;

    public THLocalAsActToPendRemAspDwn(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    public boolean process(FSMState state) {
        try {
            AspImpl remAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

            if (this.asImpl.getTrafficModeType().getMode() == TrafficModeType.Broadcast) {
                // We don't support this
                return false;

            }

            if (this.asImpl.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
                this.lbCount = 0;

                Iterator<Entry<String, Asp>> iterator=this.asImpl.appServerProcs.entrySet().iterator();
                while(iterator.hasNext()) {
                    AspImpl remAspImpl = (AspImpl) iterator.next().getValue();
                
                    FSM aspPeerFSM = remAspImpl.getPeerFSM();
                    AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

                    if (aspState == AspState.ACTIVE) {
                        this.lbCount++;
                    }
                }// for

                if (this.lbCount >= this.asImpl.getMinAspActiveForLb()) {
                    // we still have more ASP's ACTIVE for lb. Don't change
                    // state
                    return false;
                }

                if (this.lbCount > 0) {
                    // In any case if we have at least one ASP that can take
                    // care of traffic, don't change state

                    // But we are below threshold. Send "Ins. ASPs" to INACTIVE
                    // ASP's but not to ASP that caused this transition as it is
                    // already DOWN
                	iterator=this.asImpl.appServerProcs.entrySet().iterator();
                    while(iterator.hasNext()) {
                        AspImpl remAspTemp = (AspImpl) iterator.next().getValue();
                        
                        FSM aspPeerFSM = remAspTemp.getPeerFSM();
                        AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

                        if (aspState == AspState.INACTIVE) {
                            Notify notify = this.createNotify(remAsp, Status.STATUS_Other,
                                    Status.INFO_Insufficient_ASP_Resources_Active);
                            remAspTemp.getAspFactory().write(notify);
                        }
                    }

                    return false;
                }

            }// If Loadshare

            // We have reached here means AS is transitioning to be PENDING.
            // Send new AS STATUS to all INACTIVE APS's

            Iterator<Entry<String, Asp>> iterator=this.asImpl.appServerProcs.entrySet().iterator();
            while(iterator.hasNext()) {
            	remAsp = (AspImpl) iterator.next().getValue();
                
                FSM aspPeerFSM = remAsp.getPeerFSM();
                AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

                if (aspState == AspState.INACTIVE) {
                    Notify notify = this.createNotify(remAsp, Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING);
                    remAsp.getAspFactory().write(notify);
                }
            }

        } catch (Exception e) {
            logger.error(String.format("Error while translating Rem AS to PENDING. %s", this.fsm.toString()), e);
        }

        return true;
    }

    private Notify createNotify(AspImpl remAsp, int type, int info) {
        Notify msg = (Notify) this.asImpl.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

        Status status = this.asImpl.getParameterFactory().createStatus(type, info);
        msg.setStatus(status);

        if (remAsp.getASPIdentifier() != null) {
            msg.setASPIdentifier(remAsp.getASPIdentifier());
        }

        if (this.asImpl.getRoutingContext() != null) {
            msg.setRoutingContext(this.asImpl.getRoutingContext());
        }

        return msg;
    }

}
