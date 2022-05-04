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
import org.restcomm.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class SEHPeerAsStateEnterPen extends SEHAsStateEnterPen {

    private static final Logger logger = LogManager.getLogger(SEHPeerAsStateEnterPen.class);

    /**
     * @param asImpl
     * @param fsm
     */
    public SEHPeerAsStateEnterPen(AsImpl asImpl, FSM fsm) {
        super(asImpl, fsm);
    }

    @Override
    public void onEvent(FSMState state) {
        super.onEvent(state);

        // If there is even one ASP in INACTIVE state for this AS, ACTIVATE it
        Iterator<Entry<String, Asp>> iterator=this.asImpl.appServerProcs.entrySet().iterator();
        while(iterator.hasNext()) {
            AspImpl aspImpl = (AspImpl) iterator.next().getValue();
            
            FSM aspLocalFSM = aspImpl.getLocalFSM();

            if (AspState.getState(aspLocalFSM.getState().getName()) == AspState.INACTIVE) {
                AspFactoryImpl aspFactoryImpl = aspImpl.getAspFactory();
                aspFactoryImpl.sendAspActive(this.asImpl);

                // Transition the state of ASP to ACTIVE_SENT
                try {
                    aspLocalFSM.signal(TransitionState.ASP_ACTIVE_SENT);
                } catch (UnknownTransitionException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
