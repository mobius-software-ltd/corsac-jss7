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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CallBarringFeatureImpl implements CallBarringFeature {
	@ASNChoise(defaultImplementation = BasicServiceCodeImpl.class)
    private BasicServiceCode basicServiceCode;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = SSStatusImpl.class)
    private SSStatus ssStatus;

    public CallBarringFeatureImpl() {
    }

    public CallBarringFeatureImpl(BasicServiceCode basicServiceCode, SSStatus ssStatus) {
    	this.basicServiceCode=basicServiceCode;
        this.ssStatus = ssStatus;
    }

    public BasicServiceCode getBasicService() {
        return basicServiceCode;
    }

    public SSStatus getSsStatus() {
        return ssStatus;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallBarringFeature [");

        if (this.basicServiceCode != null) {
            sb.append("basicService=");
            sb.append(this.basicServiceCode);
        }

        if (this.ssStatus != null) {
            sb.append(", ssStatus=");
            sb.append(this.ssStatus);
        }

        sb.append("]");
        return sb.toString();
    }

}
