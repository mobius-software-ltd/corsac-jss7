/*
 * Mobius Software LTD
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

import org.restcomm.protocols.ss7.map.api.service.oam.SGSNEventList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class SGSNEventListImpl extends ASNBitString implements SGSNEventList {
	static final int _ID_pdpContext = 0;
    static final int _ID_moMtSms = 1;
    static final int _ID_rauGprsAttachGprsDetach = 2;
    static final int _ID_mbmsContext = 3;

    public SGSNEventListImpl() {
    	super("SGSNEventList",3,15,false);
    }

    public SGSNEventListImpl(boolean pdpContext, boolean moMtSms, boolean rauGprsAttachGprsDetach, boolean mbmsContext) {
    	super("SGSNEventList",3,15,false);
    	if (pdpContext)
            this.setBit(_ID_pdpContext);
        if (moMtSms)
            this.setBit(_ID_moMtSms);
        if (rauGprsAttachGprsDetach)
            this.setBit(_ID_rauGprsAttachGprsDetach);
        if (mbmsContext)
            this.setBit(_ID_mbmsContext);
    }

    public boolean getPdpContext() {
        return this.isBitSet(_ID_pdpContext);
    }

    public boolean getMoMtSms() {
        return this.isBitSet(_ID_moMtSms);
    }

    public boolean getRauGprsAttachGprsDetach() {
        return this.isBitSet(_ID_rauGprsAttachGprsDetach);
    }

    public boolean getMbmsContext() {
        return this.isBitSet(_ID_mbmsContext);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SGSNEventList [");

        if (this.getPdpContext()) {
            sb.append("pdpContext, ");
        }
        if (this.getMoMtSms()) {
            sb.append("moMtSms, ");
        }
        if (this.getRauGprsAttachGprsDetach()) {
            sb.append("rauGprsAttachGprsDetach, ");
        }
        if (this.getMbmsContext()) {
            sb.append("mbmsContext, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
