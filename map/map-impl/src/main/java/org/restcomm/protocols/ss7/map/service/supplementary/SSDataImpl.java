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

import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.primitives.ASNEMLPPPriorityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SSDataImpl implements SSData {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = SSCodeImpl.class)
	private SSCode ssCode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = SSStatusImpl.class)
    private SSStatus ssStatus;
    
    @ASNChoise(defaultImplementation = SSSubscriptionOptionImpl.class)
    private SSSubscriptionOption ssSubscriptionOption;
    
    private BasicServiceCodeListWrapperImpl basicServiceGroupList;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=-1)
    private ASNEMLPPPriorityImpl defaultPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNInteger nbrUser;

    public SSDataImpl() {
    }

    public SSDataImpl(SSCode ssCode, SSStatus ssStatus, SSSubscriptionOption ssSubscriptionOption, List<BasicServiceCode> basicServiceGroupList,
            EMLPPPriority defaultPriority, Integer nbrUser) {
        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        this.ssSubscriptionOption=ssSubscriptionOption;
        
        if(basicServiceGroupList!=null)
        	this.basicServiceGroupList = new BasicServiceCodeListWrapperImpl(basicServiceGroupList);
        
        if(defaultPriority!=null)
        	this.defaultPriority = new ASNEMLPPPriorityImpl(defaultPriority);
        	
        if(nbrUser!=null)
        	this.nbrUser = new ASNInteger(nbrUser,"NBRUser",1,7,false);        	
    }

    public SSCode getSsCode() {
        return ssCode;
    }

    public SSStatus getSsStatus() {
        return ssStatus;
    }

    public SSSubscriptionOption getSsSubscriptionOption() {
        return ssSubscriptionOption;
    }

    public List<BasicServiceCode> getBasicServiceGroupList() {
    	if(basicServiceGroupList==null)
    		return null;
    	
        return basicServiceGroupList.getBasicServiceCodes();
    }

    public EMLPPPriority getDefaultPriority() {
    	if(defaultPriority==null)
    		return null;
    	
        return defaultPriority.getType();
    }

    public Integer getNbrUser() {
    	if(nbrUser==null)
    		return null;
    	
        return nbrUser.getIntValue();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SSData [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode);
            sb.append(", ");
        }
        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus);
            sb.append(", ");
        }
        if (this.ssSubscriptionOption != null) {
            sb.append("ssSubscriptionOption=");
            sb.append(this.ssSubscriptionOption);
            sb.append(", ");
        }

        if (this.basicServiceGroupList != null && this.basicServiceGroupList.getBasicServiceCodes()!=null) {
            sb.append("basicServiceGroupList=[");
            boolean firstItem = true;
            for (BasicServiceCode be : this.basicServiceGroupList.getBasicServiceCodes()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.defaultPriority != null) {
            sb.append("defaultPriority=");
            sb.append(this.defaultPriority.getType());
            sb.append(", ");
        }
        if (this.nbrUser != null) {
            sb.append("nbrUser=");
            sb.append(this.nbrUser.getValue());
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(basicServiceGroupList!=null && basicServiceGroupList.getBasicServiceCodes()!=null && basicServiceGroupList.getBasicServiceCodes().size()>13)
			throw new ASNParsingComponentException("SS info should be set for register SS response", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}