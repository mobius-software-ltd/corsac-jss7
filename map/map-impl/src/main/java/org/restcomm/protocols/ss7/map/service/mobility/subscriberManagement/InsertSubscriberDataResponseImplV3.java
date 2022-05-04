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
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
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
public class InsertSubscriberDataResponseImplV3 extends MobilityMessageImpl implements InsertSubscriberDataResponse {
	private static final long serialVersionUID = 1L;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ExtTeleserviceCodeListWrapperImpl teleserviceList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private ExtBearerServiceCodeListWrapperImpl bearerServiceList = null;   
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private SSCodeListWrapperImpl ssList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1,defaultImplementation = ODBGeneralDataImpl.class)
    private ODBGeneralData odbGeneralData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNRegionalSubscriptionResponse regionalSubscriptionResponse = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = SupportedCamelPhasesImpl.class)
    private SupportedCamelPhases supportedCamelPhases = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = OfferedCamel4CSIsImpl.class)
    private OfferedCamel4CSIs offeredCamel4CSIs = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = SupportedFeaturesImpl.class)
    private SupportedFeatures supportedFeatures = null;

    public InsertSubscriberDataResponseImplV3() {
    }
    
    // For outgoing messages - MAP V3
    public InsertSubscriberDataResponseImplV3(List<ExtTeleserviceCode> teleserviceList,
            List<ExtBearerServiceCode> bearerServiceList, List<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhases supportedCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs, SupportedFeatures supportedFeatures) {

        if(teleserviceList!=null)
        	this.teleserviceList = new ExtTeleserviceCodeListWrapperImpl(teleserviceList);
        
        if(bearerServiceList!=null)
        this.bearerServiceList = new ExtBearerServiceCodeListWrapperImpl(bearerServiceList);
        
        if(ssList!=null)
        	this.ssList = new SSCodeListWrapperImpl(ssList);
        
        this.odbGeneralData = odbGeneralData;
        
        if(regionalSubscriptionResponse!=null)
        	this.regionalSubscriptionResponse = new ASNRegionalSubscriptionResponse(regionalSubscriptionResponse);
        	
        this.supportedCamelPhases = supportedCamelPhases;
        this.extensionContainer = extensionContainer;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
        this.supportedFeatures = supportedFeatures;        
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.insertSubscriberData_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.insertSubscriberData;
    }

    @Override
    public List<ExtTeleserviceCode> getTeleserviceList() {
    	if(this.teleserviceList==null)
    		return null;
    	
        return this.teleserviceList.getExtTeleserviceCode();
    }

    @Override
    public List<ExtBearerServiceCode> getBearerServiceList() {
    	if(this.bearerServiceList==null)
    		return null;
    	
        return this.bearerServiceList.getExtBearerServiceCode();
    }

    @Override
    public List<SSCode> getSSList() {
    	if(this.ssList==null)
    		return null;
    	
        return this.ssList.getSSCode();
    }

    @Override
    public ODBGeneralData getODBGeneralData() {
        return this.odbGeneralData;
    }

    @Override
    public RegionalSubscriptionResponse getRegionalSubscriptionResponse() {
    	if(this.regionalSubscriptionResponse==null)
    		return null;
    	
        return this.regionalSubscriptionResponse.getType();
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhases() {
        return this.supportedCamelPhases;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return this.offeredCamel4CSIs;
    }

    @Override
    public SupportedFeatures getSupportedFeatures() {
        return this.supportedFeatures;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InsertSubscriberDataResponse [");

        if (this.teleserviceList != null && this.teleserviceList.getExtTeleserviceCode()!=null) {
            sb.append("teleserviceList=[");
            boolean firstItem = true;
            for (ExtTeleserviceCode be : this.teleserviceList.getExtTeleserviceCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.bearerServiceList != null && this.bearerServiceList.getExtBearerServiceCode()!=null) {
            sb.append("bearerServiceList=[");
            boolean firstItem = true;
            for (ExtBearerServiceCode be : this.bearerServiceList.getExtBearerServiceCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.ssList != null && this.ssList.getSSCode()!=null) {
            sb.append("ssList=[");
            boolean firstItem = true;
            for (SSCode be : this.ssList.getSSCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.odbGeneralData != null) {
            sb.append("odbGeneralData=");
            sb.append(odbGeneralData.toString());
            sb.append(", ");
        }

        if (this.regionalSubscriptionResponse != null) {
            sb.append("regionalSubscriptionResponse=");
            sb.append(regionalSubscriptionResponse.getType());
            sb.append(", ");
        }

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(supportedCamelPhases.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        if (this.offeredCamel4CSIs != null) {
            sb.append("offeredCamel4CSIs=");
            sb.append(offeredCamel4CSIs.toString());
            sb.append(", ");
        }

        if (this.supportedFeatures != null) {
            sb.append("supportedFeatures=");
            sb.append(supportedFeatures.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(teleserviceList!=null && teleserviceList.getExtTeleserviceCode()!=null && teleserviceList.getExtTeleserviceCode().size()>20)
			throw new ASNParsingComponentException("teleservice list size should be between 1 and 20 for insert subscriber data response", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(bearerServiceList!=null && bearerServiceList.getExtBearerServiceCode()!=null && bearerServiceList.getExtBearerServiceCode().size()>50)
			throw new ASNParsingComponentException("bearer list size should be between 1 and 50 for insert subscriber data response", ASNParsingComponentExceptionReason.MistypedRootParameter);
		
		if(ssList!=null && ssList.getSSCode()!=null && ssList.getSSCode().size()>30)
			throw new ASNParsingComponentException("ss code size should be between 1 and 30 for insert subscriber data response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}

}
