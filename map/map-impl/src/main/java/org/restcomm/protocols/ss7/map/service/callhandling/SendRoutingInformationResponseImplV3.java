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

package org.restcomm.protocols.ss7.map.service.callhandling;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ASNUnavailabilityCauseImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.AllowedServicesImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CCBSIndicatorsImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfoWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ASNNumberPortabilityStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/*
 *
 * @author cristian veliscu
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,lengthIndefinite=false)
public class SendRoutingInformationResponseImplV3 extends CallHandlingMessageImpl implements SendRoutingInformationResponse {
	private static final long serialVersionUID = 1L;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
	private IMSIImpl imsi;
    
	@ASNChoise
    private ExtendedRoutingInfoImpl extRoutingInfo;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
	private CUGCheckInfoImpl cugCheckInfo;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
	private ASNNull cugSubscriptionFlag;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
	private SubscriberInfoImpl subscriberInfo;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
	private SSCodeListWrapperImpl ssList;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
	private ExtBasicServiceCodeWrapperImpl basicService;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
	private ASNNull forwardingInterrogationRequired;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
	private ISDNAddressStringImpl vmscAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
	private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
	private NAEAPreferredCIImpl naeaPreferredCI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
	private CCBSIndicatorsImpl ccbsIndicators;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
	private ISDNAddressStringImpl msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
	private ASNNumberPortabilityStatusImpl nrPortabilityStatus;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
	private ASNInteger istAlertTimer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)
	private SupportedCamelPhasesImpl supportedCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)
	private OfferedCamel4CSIsImpl offeredCamel4CSIs;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1)
	private RoutingInfoWrapperImpl routingInfo2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=true,index=-1)
	private SSCodeListWrapperImpl ssList2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=true,index=-1)
	private ExtBasicServiceCodeWrapperImpl basicService2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1)
	private AllowedServicesImpl allowedServices;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)
	private ASNUnavailabilityCauseImpl unavailabilityCause;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=false,index=-1)
	private ASNNull releaseResourcesSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=true,index=-1)
	private ExternalSignalInfoImpl gsmBearerCapability;
    
    private long mapProtocolVersion;

    

    // private static final int TAG_camelRoutingInfo = 8;

    private static final String _PrimitiveName = "SendRoutingInformationResponse";

    public SendRoutingInformationResponseImplV3() {
        this(3);
    }

    public SendRoutingInformationResponseImplV3(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationResponseImplV3(long mapProtocolVersion, IMSIImpl imsi, ExtendedRoutingInfoImpl extRoutingInfo,
            CUGCheckInfoImpl cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfoImpl subscriberInfo, List<SSCodeImpl> ssList,
            ExtBasicServiceCodeImpl basicService, boolean forwardingInterrogationRequired, ISDNAddressStringImpl vmscAddress,
            MAPExtensionContainerImpl extensionContainer, NAEAPreferredCIImpl naeaPreferredCI, CCBSIndicatorsImpl ccbsIndicators,
            ISDNAddressStringImpl msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhasesImpl supportedCamelPhases, OfferedCamel4CSIsImpl offeredCamel4CSIs, RoutingInfoImpl routingInfo2,
            List<SSCodeImpl> ssList2, ExtBasicServiceCodeImpl basicService2, AllowedServicesImpl allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfoImpl gsmBearerCapability) {

        this.imsi = imsi;
        this.extRoutingInfo = extRoutingInfo;
        this.cugCheckInfo = cugCheckInfo;
        
        if(cugSubscriptionFlag)
        	this.cugSubscriptionFlag = new ASNNull();
        
        this.subscriberInfo = subscriberInfo;
        
        if(ssList!=null)
        	this.ssList = new SSCodeListWrapperImpl(ssList);
        
        if(basicService!=null)
        	this.basicService = new ExtBasicServiceCodeWrapperImpl(basicService);
        
        if(forwardingInterrogationRequired)
        	this.forwardingInterrogationRequired = new ASNNull();
        
        this.vmscAddress = vmscAddress;
        this.extensionContainer = extensionContainer;
        this.naeaPreferredCI = naeaPreferredCI;
        this.ccbsIndicators = ccbsIndicators;
        this.msisdn = msisdn;
        
        if(nrPortabilityStatus!=null) {
        	this.nrPortabilityStatus = new ASNNumberPortabilityStatusImpl();
        	this.nrPortabilityStatus.setType(nrPortabilityStatus);
        }
        
        if(istAlertTimer!=null) {
        	this.istAlertTimer = new ASNInteger();
        	this.istAlertTimer.setValue(istAlertTimer.longValue());
        }
        
        this.supportedCamelPhases = supportedCamelPhases;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
        
        if(routingInfo2!=null)
        	this.routingInfo2 = new RoutingInfoWrapperImpl(routingInfo2);
        
        if(ssList2!=null)
        	this.ssList2 = new SSCodeListWrapperImpl(ssList2);
        
        if(basicService2!=null)
        	this.basicService2 = new ExtBasicServiceCodeWrapperImpl(basicService2);
        
        this.allowedServices = allowedServices;
        
        if(unavailabilityCause!=null) {
        	this.unavailabilityCause = new ASNUnavailabilityCauseImpl();
        	this.unavailabilityCause.setType(unavailabilityCause);
        }
        
        if(releaseResourcesSupported)
        	this.releaseResourcesSupported = new ASNNull();
        
        this.gsmBearerCapability = gsmBearerCapability;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public IMSIImpl getIMSI() {
        return this.imsi;
    }

    @Override
    public ExtendedRoutingInfoImpl getExtendedRoutingInfo() {
        return this.extRoutingInfo;
    }

    @Override
    public CUGCheckInfoImpl getCUGCheckInfo() {
        return this.cugCheckInfo;
    }

    @Override
    public boolean getCUGSubscriptionFlag() {
        return this.cugSubscriptionFlag!=null;
    }

    @Override
    public SubscriberInfoImpl getSubscriberInfo() {
        return this.subscriberInfo;
    }

    @Override
    public List<SSCodeImpl> getSSList() {
    	if(this.ssList==null)
    		return null;
    	
        return this.ssList.getSSCode();
    }

    @Override
    public ExtBasicServiceCodeImpl getBasicService() {
    	if(this.basicService==null)
    		return null;
    	
        return this.basicService.getExtBasicServiceCode();
    }

    @Override
    public boolean getForwardingInterrogationRequired() {
        return this.forwardingInterrogationRequired!=null;
    }

    @Override
    public ISDNAddressStringImpl getVmscAddress() {
        return this.vmscAddress;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public NAEAPreferredCIImpl getNaeaPreferredCI() {
        return this.naeaPreferredCI;
    }

    @Override
    public CCBSIndicatorsImpl getCCBSIndicators() {
        return this.ccbsIndicators;
    }

    @Override
    public ISDNAddressStringImpl getMsisdn() {
        return this.msisdn;
    }

    @Override
    public NumberPortabilityStatus getNumberPortabilityStatus() {
    	if(this.nrPortabilityStatus==null)
    		return null;
    	
        return this.nrPortabilityStatus.getType();
    }

    @Override
    public Integer getISTAlertTimer() {
    	if(this.istAlertTimer==null)
    		return null;
    	
        return this.istAlertTimer.getValue().intValue();
    }

    @Override
    public SupportedCamelPhasesImpl getSupportedCamelPhasesInVMSC() {
        return this.supportedCamelPhases;
    }

    @Override
    public OfferedCamel4CSIsImpl getOfferedCamel4CSIsInVMSC() {
        return this.offeredCamel4CSIs;
    }

    @Override
    public RoutingInfoImpl getRoutingInfo2() {
    	if(this.routingInfo2==null)
    		return null;
    	
        return this.routingInfo2.getRoutingInfo();
    }

    @Override
    public List<SSCodeImpl> getSSList2() {
    	if(this.ssList2==null)
    		return null;
    	
        return this.ssList2.getSSCode();
    }

    @Override
    public ExtBasicServiceCodeImpl getBasicService2() {
    	if(this.basicService2==null)
    		return null;
    	
        return this.basicService2.getExtBasicServiceCode();
    }

    @Override
    public AllowedServicesImpl getAllowedServices() {
        return this.allowedServices;
    }

    @Override
    public UnavailabilityCause getUnavailabilityCause() {
    	if(this.unavailabilityCause==null)
    		return null;
    	
        return this.unavailabilityCause.getType();
    }

    @Override
    public boolean getReleaseResourcesSupported() {
        return this.releaseResourcesSupported!=null;
    }

    @Override
    public ExternalSignalInfoImpl getGsmBearerCapability() {
        return this.gsmBearerCapability;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfo_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi);
        }

        if (this.extRoutingInfo != null) {
            sb.append(", extRoutingInfo=");
            sb.append(this.extRoutingInfo);
        }

        if (this.cugCheckInfo != null) {
            sb.append(", cugCheckInfo=");
            sb.append(this.cugCheckInfo);
        }

        if (this.cugSubscriptionFlag != null)
            sb.append(", cugSubscriptionFlag=TRUE");

        if (this.subscriberInfo != null) {
            sb.append(", subscriberInfo=");
            sb.append(this.subscriberInfo);
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
            sb.append("]");
        }

        if (this.basicService != null && this.basicService.getExtBasicServiceCode()!=null) {
            sb.append(", basicService=");
            sb.append(this.basicService.getExtBasicServiceCode());
        }

        if (this.forwardingInterrogationRequired != null)
            sb.append(", forwardingInterrogationRequired=TRUE");

        if (this.vmscAddress != null) {
            sb.append(", vmscAddress=");
            sb.append(this.vmscAddress);
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (this.naeaPreferredCI != null) {
            sb.append(", naeaPreferredCI=");
            sb.append(this.naeaPreferredCI);
        }

        if (this.ccbsIndicators != null) {
            sb.append(", ccbsIndicators=");
            sb.append(this.ccbsIndicators);
        }

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn);
        }

        if (this.nrPortabilityStatus != null) {
            sb.append(", nrPortabilityStatus=");
            sb.append(this.nrPortabilityStatus);
        }

        if (this.istAlertTimer != null) {
            sb.append(", istAlertTimer=");
            sb.append(this.istAlertTimer);
        }

        if (this.supportedCamelPhases != null) {
            sb.append(", supportedCamelPhases=");
            sb.append(this.supportedCamelPhases);
        }

        if (this.offeredCamel4CSIs != null) {
            sb.append(", offeredCamel4CSIs=");
            sb.append(this.offeredCamel4CSIs);
        }

        if (this.routingInfo2 != null && this.routingInfo2.getRoutingInfo()!=null) {
            sb.append(", routingInfo2=");
            sb.append(this.routingInfo2.getRoutingInfo());
        }

        if (this.ssList2 != null && this.ssList2.getSSCode()!=null) {
            sb.append("ssList2=[");
            boolean firstItem = true;
            for (SSCodeImpl be : this.ssList2.getSSCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }

        if (this.basicService2 != null && this.basicService2.getExtBasicServiceCode()!=null) {
            sb.append(", basicService2=");
            sb.append(this.basicService2.getExtBasicServiceCode());
        }

        if (this.allowedServices != null) {
            sb.append(", allowedServices=");
            sb.append(this.allowedServices);
        }

        if (this.unavailabilityCause != null) {
            sb.append(", unavailabilityCause=");
            sb.append(this.unavailabilityCause);
        }

        if (this.releaseResourcesSupported != null)
            sb.append(", releaseResourcesSupported=TRUE");

        if (this.gsmBearerCapability != null) {
            sb.append(", gsmBearerCapability=");
            sb.append(this.gsmBearerCapability);
        }

        sb.append("]");
        return sb.toString();
    }
}