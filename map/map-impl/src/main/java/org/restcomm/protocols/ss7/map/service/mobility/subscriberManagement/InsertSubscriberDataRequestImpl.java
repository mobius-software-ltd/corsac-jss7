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

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ASNNetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ASNSubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AgeIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriorityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionDataListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfoListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastDataListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallDataListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class InsertSubscriberDataRequestImpl extends MobilityMessageImpl implements InsertSubscriberDataRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private IMSIImpl imsi = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ISDNAddressStringImpl msisdn = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private CategoryImpl category = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNSubscriberStatus subscriberStatus = null;       
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private ExtBearerServiceCodeListWrapperImpl bearerServiceList = null;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private ExtTeleserviceCodeListWrapperImpl teleserviceList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
    private ExtSSInfoListWrapperImpl provisionedSS = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1)
    private ODBDataImpl odbData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNNull roamingRestrictionDueToUnsupportedFeature = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private ZoneCodeListWrapperImpl regionalSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private VoiceBroadcastDataListWrapperImpl vbsSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=true,index=-1)
    private VoiceGroupCallDataListWrapperImpl vgcsSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)
    private VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=true,index=-1)
    private NAEAPreferredCIImpl naeaPreferredCI = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,index=-1)
    private GPRSSubscriptionDataImpl gprsSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=false,index=-1)
    private ASNNull roamingRestrictedInSgsnDueToUnsupportedFeature = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=24,constructed=false,index=-1)
    private ASNNetworkAccessMode networkAccessMode = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=true,index=-1)
    private LSAInformationImpl lsaInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)
    private ASNNull lmuIndicator = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=true,index=-1)
    private LCSInformationImpl lcsInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=false,index=-1)
    private ASNInteger istAlertTimer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1)
    private AgeIndicatorImpl superChargerSupportedInHLR = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=28,constructed=true,index=-1)
    private MCSSInfoImpl mcSsInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=29,constructed=false,index=-1)
    private CSAllocationRetentionPriorityImpl csAllocationRetentionPriority = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1)
    private SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)
    private ChargingCharacteristicsImpl chargingCharacteristics = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)
    private AccessRestrictionDataImpl accessRestrictionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1)
    private ASNBoolean icsIndicator = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=31,constructed=true,index=-1)
    private EPSSubscriptionDataImpl epsSubscriptionData = null;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=32,constructed=true,index=-1)
    private CSGSubscriptionDataListWrapperImpl csgSubscriptionDataList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=33,constructed=false,index=-1)
    private ASNNull ueReachabilityRequestIndicator = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=34,constructed=false,index=-1)
    private ISDNAddressStringImpl sgsnNumber = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=35,constructed=false,index=-1)
    private DiameterIdentityImpl mmeName = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=36,constructed=false,index=-1)
    private ASNInteger subscribedPeriodicRAUTAUtimer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=37,constructed=false,index=-1)
    private ASNNull vplmnLIPAAllowed = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=38,constructed=false,index=-1)
    private ASNBoolean mdtUserConsent = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=39,constructed=false,index=-1)
    private ASNInteger subscribedPeriodicLAUtimer = null;

    private long mapProtocolVersion;

    // For incoming messages
    public InsertSubscriberDataRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // For outgoing messages - MAP V2
    public InsertSubscriberDataRequestImpl(long mapProtocolVersion, IMSIImpl imsi, ISDNAddressStringImpl msisdn, CategoryImpl category,
            SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCodeImpl> bearerServiceList,
            ArrayList<ExtTeleserviceCodeImpl> teleserviceList, ArrayList<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCodeImpl> regionalSubscriptionData,
            ArrayList<VoiceBroadcastDataImpl> vbsSubscriptionData, ArrayList<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.imsi = imsi;
        this.msisdn = msisdn;
        this.category = category;
        
        if(subscriberStatus!=null) {
        	this.subscriberStatus = new ASNSubscriberStatus();
        	this.subscriberStatus.setType(subscriberStatus);
        }
        
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

    // For outgoing messages - MAP V3
    public InsertSubscriberDataRequestImpl(long mapProtocolVersion, IMSIImpl imsi, ISDNAddressStringImpl msisdn, CategoryImpl category,
            SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCodeImpl> bearerServiceList,
            ArrayList<ExtTeleserviceCodeImpl> teleserviceList, ArrayList<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCodeImpl> regionalSubscriptionData,
            ArrayList<VoiceBroadcastDataImpl> vbsSubscriptionData, ArrayList<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo, MAPExtensionContainerImpl extensionContainer,
            NAEAPreferredCIImpl naeaPreferredCI, GPRSSubscriptionDataImpl gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformationImpl lsaInformation, boolean lmuIndicator, LCSInformationImpl lcsInformation, Integer istAlertTimer,
            AgeIndicatorImpl superChargerSupportedInHLR, MCSSInfoImpl mcSsInfo,
            CSAllocationRetentionPriorityImpl csAllocationRetentionPriority, SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo,
            ChargingCharacteristicsImpl chargingCharacteristics, AccessRestrictionDataImpl accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionDataImpl epsSubscriptionData, ArrayList<CSGSubscriptionDataImpl> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressStringImpl sgsnNumber, DiameterIdentityImpl mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) {

        this.mapProtocolVersion = mapProtocolVersion;
        this.imsi = imsi;
        this.msisdn = msisdn;
        this.category = category;
        
        if(subscriberStatus!=null) {
        	this.subscriberStatus = new ASNSubscriberStatus();
        	this.subscriberStatus.setType(subscriberStatus);
        }
        
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

        if (mapProtocolVersion >= 3) {
            this.extensionContainer = extensionContainer;
            this.naeaPreferredCI = naeaPreferredCI;
            this.gprsSubscriptionData = gprsSubscriptionData;
            
            if(roamingRestrictedInSgsnDueToUnsupportedFeature)
            	this.roamingRestrictedInSgsnDueToUnsupportedFeature = new ASNNull();
            
            if(networkAccessMode!=null) {
            	this.networkAccessMode = new ASNNetworkAccessMode();
            	this.networkAccessMode.setType(networkAccessMode);
            }
            
            this.lsaInformation = lsaInformation;
            
            if(lmuIndicator)
            	this.lmuIndicator = new ASNNull();
            
            this.lcsInformation = lcsInformation;
            
            if(istAlertTimer!=null) {
            	this.istAlertTimer = new ASNInteger();
            	this.istAlertTimer.setValue(istAlertTimer.longValue());
            }
            
            this.superChargerSupportedInHLR = superChargerSupportedInHLR;
            this.mcSsInfo = mcSsInfo;
            this.csAllocationRetentionPriority = csAllocationRetentionPriority;
            this.sgsnCamelSubscriptionInfo = sgsnCamelSubscriptionInfo;
            this.chargingCharacteristics = chargingCharacteristics;
            this.accessRestrictionData = accessRestrictionData;
            
            if(icsIndicator!=null) {
            	this.icsIndicator = new ASNBoolean();
            	this.icsIndicator.setValue(icsIndicator);
            }
            
            this.epsSubscriptionData = epsSubscriptionData;
            
            if(csgSubscriptionDataList!=null)
            	this.csgSubscriptionDataList = new CSGSubscriptionDataListWrapperImpl(csgSubscriptionDataList);
            
            if(ueReachabilityRequestIndicator)
            	this.ueReachabilityRequestIndicator = new ASNNull();
            
            this.sgsnNumber = sgsnNumber;
            this.mmeName = mmeName;
            
            if(subscribedPeriodicRAUTAUtimer!=null) {
            	this.subscribedPeriodicRAUTAUtimer = new ASNInteger();
            	this.subscribedPeriodicRAUTAUtimer.setValue(subscribedPeriodicRAUTAUtimer);
            }
            
            if(vplmnLIPAAllowed)
            	this.vplmnLIPAAllowed = new ASNNull();
            
            if(mdtUserConsent!=null) {
            	this.mdtUserConsent = new ASNBoolean();
            	this.mdtUserConsent.setValue(mdtUserConsent);
            }
            
            if(subscribedPeriodicLAUtimer!=null) {
            	this.subscribedPeriodicLAUtimer = new ASNInteger();
            	this.subscribedPeriodicLAUtimer.setValue(subscribedPeriodicLAUtimer);
            }
        }
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
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
    public IMSIImpl getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressStringImpl getMsisdn() {
        return this.msisdn;
    }

    @Override
    public CategoryImpl getCategory() {
        return this.category;
    }

    @Override
    public SubscriberStatus getSubscriberStatus() {
    	if(this.subscriberStatus==null)
    		return null;
    	
        return this.subscriberStatus.getType();
    }

    @Override
    public ArrayList<ExtBearerServiceCodeImpl> getBearerServiceList() {
    	if(this.bearerServiceList==null)
    		return null;
    	
        return this.bearerServiceList.getExtBearerServiceCode();
    }

    @Override
    public ArrayList<ExtTeleserviceCodeImpl> getTeleserviceList() {
    	if(this.teleserviceList==null)
    		return null;
    				
        return this.teleserviceList.getExtTeleserviceCode();
    }

    @Override
    public ArrayList<ExtSSInfoImpl> getProvisionedSS() {
    	if(this.provisionedSS==null)
    		return null;
    	
        return this.provisionedSS.getExtSSInfo();
    }

    @Override
    public ODBDataImpl getODBData() {
        return this.odbData;
    }

    @Override
    public boolean getRoamingRestrictionDueToUnsupportedFeature() {    	
        return this.roamingRestrictionDueToUnsupportedFeature!=null;
    }

    @Override
    public ArrayList<ZoneCodeImpl> getRegionalSubscriptionData() {
    	if(this.regionalSubscriptionData==null)
    		return null;
    	
        return this.regionalSubscriptionData.getZoneCode();
    }

    @Override
    public ArrayList<VoiceBroadcastDataImpl> getVbsSubscriptionData() {
    	if(this.vbsSubscriptionData==null)
    		return null;
    	
        return this.vbsSubscriptionData.getVoiceBroadcastData();
    }

    @Override
    public ArrayList<VoiceGroupCallDataImpl> getVgcsSubscriptionData() {
    	if(this.vgcsSubscriptionData==null)
    		return null;
    	
        return this.vgcsSubscriptionData.getVoiceGroupCallData();
    }

    @Override
    public VlrCamelSubscriptionInfoImpl getVlrCamelSubscriptionInfo() {
        return this.vlrCamelSubscriptionInfo;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public NAEAPreferredCIImpl getNAEAPreferredCI() {
        return this.naeaPreferredCI;
    }

    @Override
    public GPRSSubscriptionDataImpl getGPRSSubscriptionData() {
        return this.gprsSubscriptionData;
    }

    @Override
    public boolean getRoamingRestrictedInSgsnDueToUnsupportedFeature() {
        return this.roamingRestrictedInSgsnDueToUnsupportedFeature!=null;
    }

    @Override
    public NetworkAccessMode getNetworkAccessMode() {
    	if(this.networkAccessMode==null)
    		return null;
    	
        return this.networkAccessMode.getType();
    }

    @Override
    public LSAInformationImpl getLSAInformation() {
        return this.lsaInformation;
    }

    @Override
    public boolean getLmuIndicator() {
        return this.lmuIndicator!=null;
    }

    @Override
    public LCSInformationImpl getLCSInformation() {
        return this.lcsInformation;
    }

    @Override
    public Integer getIstAlertTimer() {
    	if(this.istAlertTimer==null)
    		return null;
    	
        return this.istAlertTimer.getValue().intValue();
    }

    @Override
    public AgeIndicatorImpl getSuperChargerSupportedInHLR() {
        return this.superChargerSupportedInHLR;
    }

    @Override
    public MCSSInfoImpl getMcSsInfo() {
        return this.mcSsInfo;
    }

    @Override
    public CSAllocationRetentionPriorityImpl getCSAllocationRetentionPriority() {
        return this.csAllocationRetentionPriority;
    }

    @Override
    public SGSNCAMELSubscriptionInfoImpl getSgsnCamelSubscriptionInfo() {
        return this.sgsnCamelSubscriptionInfo;
    }

    @Override
    public ChargingCharacteristicsImpl getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    @Override
    public AccessRestrictionDataImpl getAccessRestrictionData() {
        return this.accessRestrictionData;
    }

    @Override
    public Boolean getIcsIndicator() {
    	if(this.icsIndicator==null)
    		return null;
    	
        return this.icsIndicator.getValue();
    }

    @Override
    public EPSSubscriptionDataImpl getEpsSubscriptionData() {
        return this.epsSubscriptionData;
    }

    @Override
    public ArrayList<CSGSubscriptionDataImpl> getCsgSubscriptionDataList() {
    	if(this.csgSubscriptionDataList==null)
    		return null;
    	
        return this.csgSubscriptionDataList.getCSGSubscriptionDataList();
    }

    @Override
    public boolean getUeReachabilityRequestIndicator() {
        return this.ueReachabilityRequestIndicator!=null;
    }

    @Override
    public ISDNAddressStringImpl getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public DiameterIdentityImpl getMmeName() {
        return this.mmeName;
    }

    @Override
    public Long getSubscribedPeriodicRAUTAUtimer() {
    	if(this.subscribedPeriodicRAUTAUtimer==null)
    		return null;
    	
        return this.subscribedPeriodicRAUTAUtimer.getValue();
    }

    @Override
    public boolean getVplmnLIPAAllowed() {
        return this.vplmnLIPAAllowed!=null;
    }

    @Override
    public Boolean getMdtUserConsent() {
    	if(this.mdtUserConsent==null)
    		return null;
    	
        return this.mdtUserConsent.getValue();
    }

    @Override
    public Long getSubscribedPeriodicLAUtimer() {
    	if(this.subscribedPeriodicLAUtimer==null)
    		return null;
    	
        return this.subscribedPeriodicLAUtimer.getValue();
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
            for (ExtBearerServiceCodeImpl be : this.bearerServiceList.getExtBearerServiceCode()) {
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
            for (ExtTeleserviceCodeImpl be : this.teleserviceList.getExtTeleserviceCode()) {
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
            for (ExtSSInfoImpl be : this.provisionedSS.getExtSSInfo()) {
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
            for (ZoneCodeImpl be : this.regionalSubscriptionData.getZoneCode()) {
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
            for (VoiceBroadcastDataImpl be : this.vbsSubscriptionData.getVoiceBroadcastData()) {
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
            for (VoiceGroupCallDataImpl be : this.vgcsSubscriptionData.getVoiceGroupCallData()) {
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

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.naeaPreferredCI != null) {
            sb.append("naeaPreferredCI=");
            sb.append(this.naeaPreferredCI.toString());
            sb.append(", ");
        }

        if (this.gprsSubscriptionData != null) {
            sb.append("gprsSubscriptionData=");
            sb.append(this.gprsSubscriptionData.toString());
            sb.append(", ");
        }

        if (this.roamingRestrictedInSgsnDueToUnsupportedFeature!=null) {
            sb.append("roamingRestrictedInSgsnDueToUnsupportedFeature, ");
        }

        if (this.networkAccessMode != null) {
            sb.append("networkAccessMode=");
            sb.append(this.networkAccessMode.getType());
            sb.append(", ");
        }

        if (this.lsaInformation != null) {
            sb.append("lsaInformation=");
            sb.append(this.lsaInformation.toString());
            sb.append(", ");
        }

        if (this.lmuIndicator!=null) {
            sb.append("lmuIndicator, ");
        }

        if (this.lcsInformation != null) {
            sb.append("lcsInformation=");
            sb.append(this.lcsInformation.toString());
            sb.append(", ");
        }

        if (this.lcsInformation != null) {
            sb.append("lcsInformation=");
            sb.append(this.lcsInformation.toString());
            sb.append(", ");
        }

        if (this.istAlertTimer != null) {
            sb.append("istAlertTimer=");
            sb.append(this.istAlertTimer.getValue());
            sb.append(", ");
        }

        if (this.superChargerSupportedInHLR != null) {
            sb.append("superChargerSupportedInHLR=");
            sb.append(this.superChargerSupportedInHLR.toString());
            sb.append(", ");
        }

        if (this.mcSsInfo != null) {
            sb.append("mcSsInfo=");
            sb.append(this.mcSsInfo.toString());
            sb.append(", ");
        }

        if (this.csAllocationRetentionPriority != null) {
            sb.append("csAllocationRetentionPriority=");
            sb.append(this.csAllocationRetentionPriority.toString());
            sb.append(", ");
        }

        if (this.sgsnCamelSubscriptionInfo != null) {
            sb.append("sgsnCamelSubscriptionInfo=");
            sb.append(this.sgsnCamelSubscriptionInfo.toString());
            sb.append(", ");
        }

        if (this.chargingCharacteristics != null) {
            sb.append("chargingCharacteristics=");
            sb.append(this.chargingCharacteristics.toString());
            sb.append(", ");
        }

        if (this.accessRestrictionData != null) {
            sb.append("accessRestrictionData=");
            sb.append(this.accessRestrictionData.toString());
            sb.append(", ");
        }

        if (this.icsIndicator != null) {
            sb.append("icsIndicator=");
            sb.append(this.icsIndicator.getValue());
            sb.append(", ");
        }

        if (this.epsSubscriptionData != null) {
            sb.append("epsSubscriptionData=");
            sb.append(this.epsSubscriptionData.toString());
            sb.append(", ");
        }

        if (this.csgSubscriptionDataList != null && this.csgSubscriptionDataList.getCSGSubscriptionDataList()!=null) {
            sb.append("csgSubscriptionDataList=[");
            boolean firstItem = true;
            for (CSGSubscriptionDataImpl be : this.csgSubscriptionDataList.getCSGSubscriptionDataList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.ueReachabilityRequestIndicator!=null) {
            sb.append("ueReachabilityRequestIndicator, ");
        }

        if (this.sgsnNumber != null) {
            sb.append("sgsnNumber=");
            sb.append(this.sgsnNumber.toString());
            sb.append(", ");
        }

        if (this.mmeName != null) {
            sb.append("mmeName=");
            sb.append(this.mmeName.toString());
            sb.append(", ");
        }

        if (this.subscribedPeriodicRAUTAUtimer != null) {
            sb.append("subscribedPeriodicRAUTAUtimer=");
            sb.append(this.subscribedPeriodicRAUTAUtimer.getValue());
            sb.append(", ");
        }

        if (this.vplmnLIPAAllowed!=null) {
            sb.append("vplmnLIPAAllowed, ");
        }

        if (this.mdtUserConsent != null) {
            sb.append("mdtUserConsent=");
            sb.append(this.mdtUserConsent.getValue());
            sb.append(", ");
        }

        if (this.subscribedPeriodicLAUtimer != null) {
            sb.append("subscribedPeriodicLAUtimer=");
            sb.append(this.subscribedPeriodicLAUtimer.getValue());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(this.mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}