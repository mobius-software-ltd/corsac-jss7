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
import org.restcomm.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

/**
 * NTFY is received stating that there are insufficient ASP in ACTIVE state. Hence check if AS has more ASP's in INACTIVE state.
 * If found make them ACTIVE
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class THPeerAsActToActNtfyInsAsp implements TransitionHandler {

    private static final Logger logger = LogManager.getLogger(THPeerAsActToActNtfyInsAsp.class);

    private AsImpl asImpl = null;
    
    public THPeerAsActToActNtfyInsAsp(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
    }

    public boolean process(FSMState state) {

        // Iterate through all the ASP for this AS and activate if they are
        // inactive
    	Iterator<Entry<String, Asp>> iterator=this.asImpl.appServerProcs.entrySet().iterator();
        while(iterator.hasNext()) {
            AspImpl aspTemp = (AspImpl) iterator.next().getValue();
            AspFactoryImpl factory = aspTemp.getAspFactory();

            FSM aspLocalFSM = aspTemp.getLocalFSM();
            AspState aspState = AspState.getState(aspLocalFSM.getState().getName());

            if (aspState == AspState.INACTIVE && factory.getStatus()) {
                factory.sendAspActive(this.asImpl);
                try {
                    aspLocalFSM.signal(TransitionState.ASP_ACTIVE_SENT);
                } catch (UnknownTransitionException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return true;
    }
}
