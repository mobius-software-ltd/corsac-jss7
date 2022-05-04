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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TDisconnectSpecificInfoImpl implements TDisconnectSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = CauseIsupImpl.class)
    private CauseIsup releaseCause;

    public TDisconnectSpecificInfoImpl() {
    }

    public TDisconnectSpecificInfoImpl(CauseIsup releaseCause) {
       this.releaseCause = releaseCause;
    }

    public CauseIsup getReleaseCause() {
        return releaseCause;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TDisconnectSpecificInfo [");
        if (this.releaseCause != null) {
            sb.append("releaseCause= [");
            sb.append(releaseCause);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
