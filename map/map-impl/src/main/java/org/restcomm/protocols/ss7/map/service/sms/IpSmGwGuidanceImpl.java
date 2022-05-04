package org.restcomm.protocols.ss7.map.service.sms;
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidance;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author eva ogallar
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class IpSmGwGuidanceImpl implements IpSmGwGuidance {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
	private ASNInteger minimumDeliveryTimeValue;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger recommendedDeliveryTimeValue;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    public IpSmGwGuidanceImpl() {
    }

    public IpSmGwGuidanceImpl(int minimumDeliveryTimeValue, int recommendedDeliveryTimeValue, MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
                
        this.minimumDeliveryTimeValue = new ASNInteger(minimumDeliveryTimeValue,"MinimumDeliveryTimeValue",30,600,false);
        this.recommendedDeliveryTimeValue = new ASNInteger(recommendedDeliveryTimeValue,"RecommendedDeliveryTimeValue",30,600,false);        
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public int getMinimumDeliveryTimeValue() {
    	if(minimumDeliveryTimeValue==null || minimumDeliveryTimeValue.getValue()==null)
    		return 0;
    	
        return minimumDeliveryTimeValue.getIntValue();
    }

    public int getRecommendedDeliveryTimeValue() {
    	if(recommendedDeliveryTimeValue==null || recommendedDeliveryTimeValue.getValue()==null)
    		return 0;
    	
        return recommendedDeliveryTimeValue.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IpSmGwGuidance [");

        if(this.minimumDeliveryTimeValue!=null) {
	        sb.append("minimumDeliveryTimeValue=");
	        sb.append(minimumDeliveryTimeValue.getValue());
	        sb.append(", ");
        }
        
        if(recommendedDeliveryTimeValue!=null) {
	        sb.append("recommendedDeliveryTimeValue=");
	        sb.append(recommendedDeliveryTimeValue.getValue());
	        sb.append(", ");
        }
        
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(minimumDeliveryTimeValue==null)
			throw new ASNParsingComponentException("minimum delivery time value should be set for ipsmgw guidance", ASNParsingComponentExceptionReason.MistypedParameter);

		if(recommendedDeliveryTimeValue==null)
			throw new ASNParsingComponentException("recommended delivery ttime should be set for ipsmgw guidance", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
