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
import org.restcomm.protocols.ss7.commonapp.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
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
import org.restcomm.protocols.ss7.map.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.AgeIndicatorImpl;

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
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=true,index=-1, defaultImplementation = NAEAPreferredCIImpl.class)
    private NAEAPreferredCI naeaPreferredCI = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,index=-1, defaultImplementation = GPRSSubscriptionDataImpl.class)
    private GPRSSubscriptionData gprsSubscriptionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=false,index=-1)
    private ASNNull roamingRestrictedInSgsnDueToUnsupportedFeature = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=24,constructed=false,index=-1)
    private ASNNetworkAccessMode networkAccessMode = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=true,index=-1, defaultImplementation = LSAInformationImpl.class)
    private LSAInformation lsaInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)
    private ASNNull lmuIndicator = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=true,index=-1, defaultImplementation = LCSInformationImpl.class)
    private LCSInformation lcsInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=false,index=-1)
    private ASNInteger istAlertTimer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1, defaultImplementation = AgeIndicatorImpl.class)
    private AgeIndicator superChargerSupportedInHLR = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=28,constructed=true,index=-1, defaultImplementation = MCSSInfoImpl.class)
    private MCSSInfo mcSsInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=29,constructed=false,index=-1, defaultImplementation = CSAllocationRetentionPriorityImpl.class)
    private CSAllocationRetentionPriority csAllocationRetentionPriority = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1, defaultImplementation = SGSNCAMELSubscriptionInfoImpl.class)
    private SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1, defaultImplementation = ChargingCharacteristicsImpl.class)
    private ChargingCharacteristics chargingCharacteristics = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1, defaultImplementation = AccessRestrictionDataImpl.class)
    private AccessRestrictionData accessRestrictionData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1)
    private ASNBoolean icsIndicator = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=31,constructed=true,index=-1, defaultImplementation = EPSSubscriptionDataImpl.class)
    private EPSSubscriptionData epsSubscriptionData = null;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=32,constructed=true,index=-1)
    private CSGSubscriptionDataListWrapperImpl csgSubscriptionDataList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=33,constructed=false,index=-1)
    private ASNNull ueReachabilityRequestIndicator = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=34,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString sgsnNumber = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=35,constructed=false,index=-1, defaultImplementation = DiameterIdentityImpl.class)
    private DiameterIdentity mmeName = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=36,constructed=false,index=-1)
    private ASNInteger subscribedPeriodicRAUTAUtimer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=37,constructed=false,index=-1)
    private ASNNull vplmnLIPAAllowed = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=38,constructed=false,index=-1)
    private ASNBoolean mdtUserConsent = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=39,constructed=false,index=-1)
    private ASNInteger subscribedPeriodicLAUtimer = null;

    private long mapProtocolVersion;

    public InsertSubscriberDataRequestImpl() {
    	this.mapProtocolVersion=3;
    }
    
    // For incoming messages
    public InsertSubscriberDataRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // For outgoing messages - MAP V2
    public InsertSubscriberDataRequestImpl(long mapProtocolVersion, IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) {
        this.mapProtocolVersion = mapProtocolVersion;
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

    // For outgoing messages - MAP V3
    public InsertSubscriberDataRequestImpl(long mapProtocolVersion, IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
            AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo,
            CSAllocationRetentionPriority csAllocationRetentionPriority, SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo,
            ChargingCharacteristics chargingCharacteristics, AccessRestrictionData accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionData epsSubscriptionData, List<CSGSubscriptionData> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressString sgsnNumber, DiameterIdentity mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) {

        this.mapProtocolVersion = mapProtocolVersion;
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

        if (mapProtocolVersion >= 3) {
            this.extensionContainer = extensionContainer;
            this.naeaPreferredCI = naeaPreferredCI;
            this.gprsSubscriptionData = gprsSubscriptionData;
            
            if(roamingRestrictedInSgsnDueToUnsupportedFeature)
            	this.roamingRestrictedInSgsnDueToUnsupportedFeature = new ASNNull();
            
            if(networkAccessMode!=null)
            	this.networkAccessMode = new ASNNetworkAccessMode(networkAccessMode);
            	
            this.lsaInformation = lsaInformation;
            
            if(lmuIndicator)
            	this.lmuIndicator = new ASNNull();
            
            this.lcsInformation = lcsInformation;
            
            if(istAlertTimer!=null)
            	this.istAlertTimer = new ASNInteger(istAlertTimer);
            	
            this.superChargerSupportedInHLR = superChargerSupportedInHLR;
            this.mcSsInfo = mcSsInfo;
            this.csAllocationRetentionPriority = csAllocationRetentionPriority;
            this.sgsnCamelSubscriptionInfo = sgsnCamelSubscriptionInfo;
            this.chargingCharacteristics = chargingCharacteristics;
            this.accessRestrictionData = accessRestrictionData;
            
            if(icsIndicator!=null)
            	this.icsIndicator = new ASNBoolean(icsIndicator);
            	
            this.epsSubscriptionData = epsSubscriptionData;
            
            if(csgSubscriptionDataList!=null)
            	this.csgSubscriptionDataList = new CSGSubscriptionDataListWrapperImpl(csgSubscriptionDataList);
            
            if(ueReachabilityRequestIndicator)
            	this.ueReachabilityRequestIndicator = new ASNNull();
            
            this.sgsnNumber = sgsnNumber;
            this.mmeName = mmeName;
            
            if(subscribedPeriodicRAUTAUtimer!=null)
            	this.subscribedPeriodicRAUTAUtimer = new ASNInteger(subscribedPeriodicRAUTAUtimer);
            	
            if(vplmnLIPAAllowed)
            	this.vplmnLIPAAllowed = new ASNNull();
            
            if(mdtUserConsent!=null)
            	this.mdtUserConsent = new ASNBoolean(mdtUserConsent);
            	
            if(subscribedPeriodicLAUtimer!=null)
            	this.subscribedPeriodicLAUtimer = new ASNInteger(subscribedPeriodicLAUtimer);            	
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
        return this.extensionContainer;
    }

    @Override
    public NAEAPreferredCI getNAEAPreferredCI() {
        return this.naeaPreferredCI;
    }

    @Override
    public GPRSSubscriptionData getGPRSSubscriptionData() {
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
    public LSAInformation getLSAInformation() {
        return this.lsaInformation;
    }

    @Override
    public boolean getLmuIndicator() {
        return this.lmuIndicator!=null;
    }

    @Override
    public LCSInformation getLCSInformation() {
        return this.lcsInformation;
    }

    @Override
    public Integer getIstAlertTimer() {
    	if(this.istAlertTimer==null)
    		return null;
    	
        return this.istAlertTimer.getIntValue();
    }

    @Override
    public AgeIndicator getSuperChargerSupportedInHLR() {
        return this.superChargerSupportedInHLR;
    }

    @Override
    public MCSSInfo getMcSsInfo() {
        return this.mcSsInfo;
    }

    @Override
    public CSAllocationRetentionPriority getCSAllocationRetentionPriority() {
        return this.csAllocationRetentionPriority;
    }

    @Override
    public SGSNCAMELSubscriptionInfo getSgsnCamelSubscriptionInfo() {
        return this.sgsnCamelSubscriptionInfo;
    }

    @Override
    public ChargingCharacteristics getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    @Override
    public AccessRestrictionData getAccessRestrictionData() {
        return this.accessRestrictionData;
    }

    @Override
    public Boolean getIcsIndicator() {
    	if(this.icsIndicator==null)
    		return null;
    	
        return this.icsIndicator.getValue();
    }

    @Override
    public EPSSubscriptionData getEpsSubscriptionData() {
        return this.epsSubscriptionData;
    }

    @Override
    public List<CSGSubscriptionData> getCsgSubscriptionDataList() {
    	if(this.csgSubscriptionDataList==null)
    		return null;
    	
        return this.csgSubscriptionDataList.getCSGSubscriptionDataList();
    }

    @Override
    public boolean getUeReachabilityRequestIndicator() {
        return this.ueReachabilityRequestIndicator!=null;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public DiameterIdentity getMmeName() {
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
            for (CSGSubscriptionData be : this.csgSubscriptionDataList.getCSGSubscriptionDataList()) {
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