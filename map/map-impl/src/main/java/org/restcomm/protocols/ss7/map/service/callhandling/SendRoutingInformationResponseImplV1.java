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

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.AllowedServicesImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CCBSIndicatorsImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/*
 *
 * @author cristian veliscu
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInformationResponseImplV1 extends CallHandlingMessageImpl implements SendRoutingInformationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private IMSIImpl imsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=2)
	private CUGCheckInfoImpl cugCheckInfo;
    
    @ASNChoise
    private RoutingInfoImpl routingInfo;
    
	private long mapProtocolVersion;

    public SendRoutingInformationResponseImplV1() {
        this(1);
    }

    public SendRoutingInformationResponseImplV1(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationResponseImplV1(IMSIImpl imsi, RoutingInfoImpl routingInfo, CUGCheckInfoImpl cugCheckInfo) {
        this(1, imsi, routingInfo, cugCheckInfo);
    }

    public SendRoutingInformationResponseImplV1(long mapProtocolVersion, IMSIImpl imsi, RoutingInfoImpl routingInfo,
            CUGCheckInfoImpl cugCheckInfo) {
        this.imsi = imsi;        
        this.routingInfo = routingInfo;
        this.cugCheckInfo = cugCheckInfo;
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
        return null;
    }

    @Override
    public CUGCheckInfoImpl getCUGCheckInfo() {
        return this.cugCheckInfo;
    }

    @Override
    public boolean getCUGSubscriptionFlag() {
        return false;
    }

    @Override
    public SubscriberInfoImpl getSubscriberInfo() {
        return null;
    }

    @Override
    public ArrayList<SSCodeImpl> getSSList() {
        return null;
    }

    @Override
    public ExtBasicServiceCodeImpl getBasicService() {
        return null;
    }

    @Override
    public boolean getForwardingInterrogationRequired() {
        return false;
    }

    @Override
    public ISDNAddressStringImpl getVmscAddress() {
        return null;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return null;
    }

    @Override
    public NAEAPreferredCIImpl getNaeaPreferredCI() {
        return null;
    }

    @Override
    public CCBSIndicatorsImpl getCCBSIndicators() {
        return null;
    }

    @Override
    public ISDNAddressStringImpl getMsisdn() {
        return null;
    }

    @Override
    public NumberPortabilityStatus getNumberPortabilityStatus() {
    	return null;    	
    }

    @Override
    public Integer getISTAlertTimer() {
    	return null;    	
    }

    @Override
    public SupportedCamelPhasesImpl getSupportedCamelPhasesInVMSC() {
        return null;
    }

    @Override
    public OfferedCamel4CSIsImpl getOfferedCamel4CSIsInVMSC() {
        return null;
    }

    @Override
    public RoutingInfoImpl getRoutingInfo2() {
        return routingInfo;
    }

    @Override
    public ArrayList<SSCodeImpl> getSSList2() {
        return null;
    }

    @Override
    public ExtBasicServiceCodeImpl getBasicService2() {
        return null;
    }

    @Override
    public AllowedServicesImpl getAllowedServices() {
        return null;
    }

    @Override
    public UnavailabilityCause getUnavailabilityCause() {
    	return null;
    }

    @Override
    public boolean getReleaseResourcesSupported() {
        return false;
    }

    @Override
    public ExternalSignalInfoImpl getGsmBearerCapability() {
        return null;
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
        sb.append("SendRoutingInformationResponse [");

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi);
        }

        if (this.routingInfo != null) {
            sb.append(", routingInfo=");
            sb.append(this.routingInfo);
        }

        if (this.cugCheckInfo != null) {
            sb.append(", cugCheckInfo=");
            sb.append(this.cugCheckInfo);
        }

        sb.append("]");
        return sb.toString();
    }
}