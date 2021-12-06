/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CallBarringFeatureImpl implements CallBarringFeature {
	@ASNChoise
    private BasicServiceCodeImpl basicServiceCode;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = SSStatusImpl.class)
    private SSStatus ssStatus;

    public CallBarringFeatureImpl() {
    }

    public CallBarringFeatureImpl(BasicServiceCode basicServiceCode, SSStatus ssStatus) {
    	if(basicServiceCode instanceof BasicServiceCodeImpl)
    		this.basicServiceCode=(BasicServiceCodeImpl)basicServiceCode;
    	else if(basicServiceCode!=null) {
    		if(basicServiceCode.getBearerService()!=null)
    			this.basicServiceCode = new BasicServiceCodeImpl(basicServiceCode.getBearerService());
    		else 
    			this.basicServiceCode = new BasicServiceCodeImpl(basicServiceCode.getTeleservice());
    	}
    	
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
