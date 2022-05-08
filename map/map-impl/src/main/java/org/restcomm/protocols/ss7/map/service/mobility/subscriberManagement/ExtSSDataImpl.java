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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtSSDataImpl implements ExtSSData {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0,defaultImplementation = SSCodeImpl.class)
	private SSCode ssCode = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=1,defaultImplementation = ExtSSStatusImpl.class)
    private ExtSSStatus ssStatus = null;
    
    @ASNChoise(defaultImplementation = SSSubscriptionOptionImpl.class)
    private SSSubscriptionOption ssSubscriptionOption = null;
    
    private ExtBasicServiceCodeListWrapperImpl basicServiceGroupList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;

    public ExtSSDataImpl() {
    }

    /**
     *
     */
    public ExtSSDataImpl(SSCode ssCode, ExtSSStatus ssStatus, SSSubscriptionOption ssSubscriptionOption,
            List<ExtBasicServiceCode> basicServiceGroupList, MAPExtensionContainer extensionContainer) {
        this.ssCode = ssCode;
        this.ssStatus = ssStatus;        
        this.ssSubscriptionOption = ssSubscriptionOption;
        
        if(basicServiceGroupList!=null)
        	this.basicServiceGroupList = new ExtBasicServiceCodeListWrapperImpl(basicServiceGroupList);
        
        this.extensionContainer = extensionContainer;
    }

    public SSCode getSsCode() {
        return this.ssCode;
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    public SSSubscriptionOption getSSSubscriptionOption() {
        return this.ssSubscriptionOption;
    }

    public List<ExtBasicServiceCode> getBasicServiceGroupList() {
    	if(this.basicServiceGroupList==null)
    		return null;
    	
        return this.basicServiceGroupList.getExtBasicServiceCode();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtSSData [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if (this.ssSubscriptionOption != null) {
            sb.append("ssSubscriptionOption=");
            sb.append(this.ssSubscriptionOption.toString());
            sb.append(", ");
        }

        if (this.basicServiceGroupList != null && this.basicServiceGroupList.getExtBasicServiceCode()!=null) {
            sb.append("basicServiceGroupList=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicServiceGroupList.getExtBasicServiceCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ssCode==null)
			throw new ASNParsingComponentException("ss code should be set for ext ss data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(ssStatus==null)
			throw new ASNParsingComponentException("ss status should be set for ext ss data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}