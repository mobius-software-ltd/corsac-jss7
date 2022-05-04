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

import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class THPeerAsActToPen implements TransitionHandler {

    private AsImpl asImpl = null;
    private FSM fsm;

    public THPeerAsActToPen(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    public boolean process(FSMState state) {
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

        // check if there is atleast one other ASP in ACTIVE state. If
        // yes this AS remains in ACTIVE state else goes in PENDING state.
        Iterator<Entry<String, Asp>> iterator=this.asImpl.appServerProcs.entrySet().iterator();
        while(iterator.hasNext()) {
            AspImpl aspImpl = (AspImpl) iterator.next().getValue();
            
            FSM aspLocalFSM = aspImpl.getLocalFSM();
            AspState aspState = AspState.getState(aspLocalFSM.getState().getName());

            if (!aspImpl.getName().equals(causeAsp.getName()) && aspState == AspState.ACTIVE) {
                return false;
            }
        }
        return true;
    }

}
