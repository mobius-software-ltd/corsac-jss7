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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.MSCSEventList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class MSCSEventListImpl extends ASNBitString implements MSCSEventList {
	static final int _ID_moMtCall = 0;
    static final int _ID_moMtSms = 1;
    static final int _ID_luImsiAttachImsiDetach = 2;
    static final int _ID_handovers = 3;
    static final int _ID_ss = 4;

    public MSCSEventListImpl() {    
    	super("MSCSEventList",4,15,false);
    }

    public MSCSEventListImpl(boolean moMtCall, boolean moMtSms, boolean luImsiAttachImsiDetach, boolean handovers, boolean ss) {
    	super("MSCSEventList",4,15,false);
    	if (moMtCall)
            this.setBit(_ID_moMtCall);
        if (moMtSms)
            this.setBit(_ID_moMtSms);
        if (luImsiAttachImsiDetach)
            this.setBit(_ID_luImsiAttachImsiDetach);
        if (handovers)
            this.setBit(_ID_handovers);
        if (ss)
            this.setBit(_ID_ss);
    }

    public boolean getMoMtCall() {
        return this.isBitSet(_ID_moMtCall);
    }

    public boolean getMoMtSms() {
        return this.isBitSet(_ID_moMtSms);
    }

    public boolean getLuImsiAttachImsiDetach() {
        return this.isBitSet(_ID_luImsiAttachImsiDetach);
    }

    public boolean getHandovers() {
        return this.isBitSet(_ID_handovers);
    }

    public boolean getSs() {
        return this.isBitSet(_ID_ss);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MSCSEventList [");

        if (this.getMoMtCall()) {
            sb.append("moMtCall, ");
        }
        if (this.getMoMtSms()) {
            sb.append("moMtSms, ");
        }
        if (this.getLuImsiAttachImsiDetach()) {
            sb.append("luImsiAttachImsiDetach, ");
        }
        if (this.getHandovers()) {
            sb.append("handovers, ");
        }
        if (this.getSs()) {
            sb.append("ss, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
