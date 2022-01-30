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

import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class InsertSubscriberDataRequestImplV1 extends MobilityMessageImpl implements InsertSubscriberDataRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = IMSIImpl.class)
    private IMSI imsi = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = CategoryImpl.class)
    private Category category = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNSubscriberStatus subscriberStatus = null;       
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private ExtBearerServiceCodeListWrapperImpl bearerServiceList = null;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private ExtTeleserviceCodeListWrapperImpl teleserviceList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
    private ExtSSInfoListWrapperImpl provisionedSS = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1,defaultImplementation = ODBDataImpl.class)
    private ODBData odbData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNNull roamingRestrictionDueToUnsupportedFeature = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private ZoneCodeListWrapperImpl regionalSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private VoiceBroadcastDataListWrapperImpl vbsSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=true,index=-1)
    private VoiceGroupCallDataListWrapperImpl vgcsSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1,defaultImplementation = VlrCamelSubscriptionInfoImpl.class)
    private VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;
    
    public InsertSubscriberDataRequestImplV1() {    	
    }

    // For outgoing messages - MAP V2
    public InsertSubscriberDataRequestImplV1(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) {
        this.imsi = imsi;
        this.msisdn = msisdn;
        this.category = category;
        
        if(subscriberStatus!=null)
        	this.subscriberStatus = new ASNSubscriberStatus(subscriberStatus);
        	
        if(bearerServiceList!=null)
        	this.bearerServiceList = new ExtBearerServiceCodeListWrapperImpl(bearerServiceList);
        
        if(teleserviceList!=null)
        	this.teleserviceList = new ExtTeleserviceCodeListWrapperImpl(teleserviceList);
        
        if(provisionedSS!=null)
        	this.provisionedSS = new ExtSSInfoListWrapperImpl(provisionedSS);
        
        this.odbData = odbData;
        
        if(roamingRestrictionDueToUnsupportedFeature)
        	this.roamingRestrictionDueToUnsupportedFeature = new ASNNull();
        
        if(regionalSubscriptionData!=null)
        	this.regionalSubscriptionData = new ZoneCodeListWrapperImpl(regionalSubscriptionData);
        
        if(vbsSubscriptionData!=null)
        	this.vbsSubscriptionData = new VoiceBroadcastDataListWrapperImpl(vbsSubscriptionData);
        
        if(vgcsSubscriptionData!=null)
        	this.vgcsSubscriptionData = new VoiceGroupCallDataListWrapperImpl(vgcsSubscriptionData);
        
        this.vlrCamelSubscriptionInfo = vlrCamelSubscriptionInfo;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.insertSubscriberData_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.insertSubscriberData;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public SubscriberStatus getSubscriberStatus() {
    	if(this.subscriberStatus==null)
    		return null;
    	
        return this.subscriberStatus.getType();
    }

    @Override
    public List<ExtBearerServiceCode> getBearerServiceList() {
    	if(this.bearerServiceList==null)
    		return null;
    	
        return this.bearerServiceList.getExtBearerServiceCode();
    }

    @Override
    public List<ExtTeleserviceCode> getTeleserviceList() {
    	if(this.teleserviceList==null)
    		return null;
    				
        return this.teleserviceList.getExtTeleserviceCode();
    }

    @Override
    public List<ExtSSInfo> getProvisionedSS() {
    	if(this.provisionedSS==null)
    		return null;
    	
        return this.provisionedSS.getExtSSInfo();
    }

    @Override
    public ODBData getODBData() {
        return this.odbData;
    }

    @Override
    public boolean getRoamingRestrictionDueToUnsupportedFeature() {    	
        return this.roamingRestrictionDueToUnsupportedFeature!=null;
    }

    @Override
    public List<ZoneCode> getRegionalSubscriptionData() {
    	if(this.regionalSubscriptionData==null)
    		return null;
    	
        return this.regionalSubscriptionData.getZoneCode();
    }

    @Override
    public List<VoiceBroadcastData> getVbsSubscriptionData() {
    	if(this.vbsSubscriptionData==null)
    		return null;
    	
        return this.vbsSubscriptionData.getVoiceBroadcastData();
    }

    @Override
    public List<VoiceGroupCallData> getVgcsSubscriptionData() {
    	if(this.vgcsSubscriptionData==null)
    		return null;
    	
        return this.vgcsSubscriptionData.getVoiceGroupCallData();
    }

    @Override
    public VlrCamelSubscriptionInfo getVlrCamelSubscriptionInfo() {
        return this.vlrCamelSubscriptionInfo;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    @Override
    public NAEAPreferredCI getNAEAPreferredCI() {
        return null;
    }

    @Override
    public GPRSSubscriptionData getGPRSSubscriptionData() {
        return null;
    }

    @Override
    public boolean getRoamingRestrictedInSgsnDueToUnsupportedFeature() {
        return false;
    }

    @Override
    public NetworkAccessMode getNetworkAccessMode() {
    	return null;
    }

    @Override
    public LSAInformation getLSAInformation() {
    	return null;
    }

    @Override
    public boolean getLmuIndicator() {
    	return false;
    }

    @Override
    public LCSInformation getLCSInformation() {
    	return null;
    }

    @Override
    public Integer getIstAlertTimer() {
    	return null;
    }

    @Override
    public AgeIndicator getSuperChargerSupportedInHLR() {
    	return null;
    }

    @Override
    public MCSSInfo getMcSsInfo() {
    	return null;
    }

    @Override
    public CSAllocationRetentionPriority getCSAllocationRetentionPriority() {
    	return null;
    }

    @Override
    public SGSNCAMELSubscriptionInfo getSgsnCamelSubscriptionInfo() {
    	return null;
    }

    @Override
    public ChargingCharacteristics getChargingCharacteristics() {
    	return null;
    }

    @Override
    public AccessRestrictionData getAccessRestrictionData() {
    	return null;
    }

    @Override
    public Boolean getIcsIndicator() {
    	return null;
    }

    @Override
    public EPSSubscriptionData getEpsSubscriptionData() {
    	return null;
    }

    @Override
    public List<CSGSubscriptionData> getCsgSubscriptionDataList() {
    	return null;
    }

    @Override
    public boolean getUeReachabilityRequestIndicator() {
        return false;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
    	return null;
    }

    @Override
    public DiameterIdentity getMmeName() {
    	return null;
    }

    @Override
    public Long getSubscribedPeriodicRAUTAUtimer() {
    	return null;
    }

    @Override
    public boolean getVplmnLIPAAllowed() {
    	return false;
    }

    @Override
    public Boolean getMdtUserConsent() {
    	return null;
    }

    @Override
    public Long getSubscribedPeriodicLAUtimer() {
    	return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InsertSubscriberDataRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
            sb.append(", ");
        }

        if (this.msisdn != null) {
            sb.append("msisdn=");
            sb.append(this.msisdn.toString());
            sb.append(", ");
        }

        if (this.category != null) {
            sb.append("category=");
            sb.append(this.category.toString());
            sb.append(", ");
        }

        if (this.subscriberStatus != null) {
            sb.append("subscriberStatus=");
            sb.append(this.subscriberStatus.getType());
            sb.append(", ");
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

        if (this.provisionedSS != null && this.provisionedSS.getExtSSInfo()!=null) {
            sb.append("provisionedSS=[");
            boolean firstItem = true;
            for (ExtSSInfo be : this.provisionedSS.getExtSSInfo()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.odbData != null) {
            sb.append("odbData=");
            sb.append(this.odbData.toString());
            sb.append(", ");
        }

        if (this.roamingRestrictionDueToUnsupportedFeature!=null) {
            sb.append("roamingRestrictionDueToUnsupportedFeature, ");
        }

        if (this.regionalSubscriptionData != null && this.regionalSubscriptionData.getZoneCode()!=null) {
            sb.append("regionalSubscriptionData=[");
            boolean firstItem = true;
            for (ZoneCode be : this.regionalSubscriptionData.getZoneCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.vbsSubscriptionData != null && this.vbsSubscriptionData.getVoiceBroadcastData()!=null) {
            sb.append("vbsSubscriptionData=[");
            boolean firstItem = true;
            for (VoiceBroadcastData be : this.vbsSubscriptionData.getVoiceBroadcastData()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.vgcsSubscriptionData != null && this.vgcsSubscriptionData.getVoiceGroupCallData()!=null) {
            sb.append("vgcsSubscriptionData=[");
            boolean firstItem = true;
            for (VoiceGroupCallData be : this.vgcsSubscriptionData.getVoiceGroupCallData()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.vlrCamelSubscriptionInfo != null) {
            sb.append("vlrCamelSubscriptionInfo=");
            sb.append(this.vlrCamelSubscriptionInfo.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}