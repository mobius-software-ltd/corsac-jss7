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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CallBarringInfoImpl implements CallBarringInfo {

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = SSCodeImpl.class)
	private SSCode ssCode;
    
	private CallBarringFeatureListWrapperImpl callBarringFeatureList;

    public CallBarringInfoImpl() {        
    }

    public CallBarringInfoImpl(SSCode ssCode, List<CallBarringFeature> callBarringFeatureList) {        
        this.ssCode = ssCode;        
        if(callBarringFeatureList!=null)
        	this.callBarringFeatureList = new CallBarringFeatureListWrapperImpl(callBarringFeatureList);        
    }

    public SSCode getSsCode() {
    	return ssCode;
    }

    public List<CallBarringFeature> getCallBarringFeatureList() {
    	if(callBarringFeatureList==null)
    		return null;
    	
        return callBarringFeatureList.getCallBarringFeatures();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallBarringInfo [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode);
        }

        if (this.callBarringFeatureList != null && this.callBarringFeatureList.getCallBarringFeatures()!=null) {
            sb.append("callBarringFeatureList=[");
            boolean firstItem = true;
            for (CallBarringFeature be : this.callBarringFeatureList.getCallBarringFeatures()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        sb.append("]");
        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(callBarringFeatureList==null || callBarringFeatureList.getCallBarringFeatures()==null || callBarringFeatureList.getCallBarringFeatures().size()==0)
			throw new ASNParsingComponentException("call barring feature list should be set for call baring info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
