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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ASNRegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class InsertSubscriberDataResponseImpl extends MobilityMessageImpl implements InsertSubscriberDataResponse {
	private static final long serialVersionUID = 1L;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ExtTeleserviceCodeListWrapperImpl teleserviceList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private ExtBearerServiceCodeListWrapperImpl bearerServiceList = null;   
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private SSCodeListWrapperImpl ssList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ODBGeneralDataImpl odbGeneralData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNRegionalSubscriptionResponse regionalSubscriptionResponse = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private SupportedCamelPhasesImpl supportedCamelPhases = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private OfferedCamel4CSIsImpl offeredCamel4CSIs = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private SupportedFeaturesImpl supportedFeatures = null;

    private long mapProtocolVersion;

    public InsertSubscriberDataResponseImpl() {
    	this.mapProtocolVersion=3;
    }
    
    // For incoming messages
    public InsertSubscriberDataResponseImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // For outgoing messages - MAP V2
    public InsertSubscriberDataResponseImpl(long mapProtocolVersion, List<ExtTeleserviceCodeImpl> teleserviceList,
            List<ExtBearerServiceCodeImpl> bearerServiceList, List<SSCodeImpl> ssList, ODBGeneralDataImpl odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) {
        this.mapProtocolVersion = mapProtocolVersion;
        
        if(teleserviceList!=null)
        	this.teleserviceList = new ExtTeleserviceCodeListWrapperImpl(teleserviceList);
        
        if(bearerServiceList!=null)
        this.bearerServiceList = new ExtBearerServiceCodeListWrapperImpl(bearerServiceList);
        
        if(ssList!=null)
        	this.ssList = new SSCodeListWrapperImpl(ssList);
        
        this.odbGeneralData = odbGeneralData;
        
        if(regionalSubscriptionResponse!=null) {
        	this.regionalSubscriptionResponse = new ASNRegionalSubscriptionResponse();
        	this.regionalSubscriptionResponse.setType(regionalSubscriptionResponse);
        }
    }

    // For outgoing messages - MAP V3
    public InsertSubscriberDataResponseImpl(long mapProtocolVersion, List<ExtTeleserviceCodeImpl> teleserviceList,
            List<ExtBearerServiceCodeImpl> bearerServiceList, List<SSCodeImpl> ssList, ODBGeneralDataImpl odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhasesImpl supportedCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIs, SupportedFeaturesImpl supportedFeatures) {

        this.mapProtocolVersion = mapProtocolVersion;
        
        if(teleserviceList!=null)
        	this.teleserviceList = new ExtTeleserviceCodeListWrapperImpl(teleserviceList);
        
        if(bearerServiceList!=null)
        this.bearerServiceList = new ExtBearerServiceCodeListWrapperImpl(bearerServiceList);
        
        if(ssList!=null)
        	this.ssList = new SSCodeListWrapperImpl(ssList);
        
        this.odbGeneralData = odbGeneralData;
        
        if(regionalSubscriptionResponse!=null) {
        	this.regionalSubscriptionResponse = new ASNRegionalSubscriptionResponse();
        	this.regionalSubscriptionResponse.setType(regionalSubscriptionResponse);
        }
        
        if (mapProtocolVersion >= 3) {
            this.supportedCamelPhases = supportedCamelPhases;
            this.extensionContainer = extensionContainer;
            this.offeredCamel4CSIs = offeredCamel4CSIs;
            this.supportedFeatures = supportedFeatures;
        }
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
    public List<ExtTeleserviceCodeImpl> getTeleserviceList() {
    	if(this.teleserviceList==null)
    		return null;
    	
        return this.teleserviceList.getExtTeleserviceCode();
    }

    @Override
    public List<ExtBearerServiceCodeImpl> getBearerServiceList() {
    	if(this.bearerServiceList==null)
    		return null;
    	
        return this.bearerServiceList.getExtBearerServiceCode();
    }

    @Override
    public List<SSCodeImpl> getSSList() {
    	if(this.ssList==null)
    		return null;
    	
        return this.ssList.getSSCode();
    }

    @Override
    public ODBGeneralDataImpl getODBGeneralData() {
        return this.odbGeneralData;
    }

    @Override
    public RegionalSubscriptionResponse getRegionalSubscriptionResponse() {
    	if(this.regionalSubscriptionResponse==null)
    		return null;
    	
        return this.regionalSubscriptionResponse.getType();
    }

    @Override
    public SupportedCamelPhasesImpl getSupportedCamelPhases() {
        return this.supportedCamelPhases;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public OfferedCamel4CSIsImpl getOfferedCamel4CSIs() {
        return this.offeredCamel4CSIs;
    }

    @Override
    public SupportedFeaturesImpl getSupportedFeatures() {
        return this.supportedFeatures;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InsertSubscriberDataResponse [");

        if (this.teleserviceList != null && this.teleserviceList.getExtTeleserviceCode()!=null) {
            sb.append("teleserviceList=[");
            boolean firstItem = true;
            for (ExtTeleserviceCodeImpl be : this.teleserviceList.getExtTeleserviceCode()) {
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
            for (ExtBearerServiceCodeImpl be : this.bearerServiceList.getExtBearerServiceCode()) {
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
            for (SSCodeImpl be : this.ssList.getSSCode()) {
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

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
