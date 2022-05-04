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

package org.restcomm.protocols.ss7.map.service.callhandling;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.service.callhandling.AllowedServices;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CCBSIndicators;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInformationResponseImplV1 extends CallHandlingMessageImpl implements SendRoutingInformationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
	private IMSI imsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=2, defaultImplementation = CUGCheckInfoImpl.class)
	private CUGCheckInfo cugCheckInfo;
    
    @ASNChoise(defaultImplementation = RoutingInfoImpl.class)
    private RoutingInfo routingInfo;
    
	public SendRoutingInformationResponseImplV1() {
    }

    public SendRoutingInformationResponseImplV1(IMSI imsi, RoutingInfo routingInfo,
    		CUGCheckInfo cugCheckInfo) {
        this.imsi = imsi;        
        this.routingInfo=routingInfo;
        this.cugCheckInfo = cugCheckInfo;
    }

    @Override
    public IMSI getIMSI() {
        return this.imsi;
    }

    @Override
    public ExtendedRoutingInfo getExtendedRoutingInfo() {
        return null;
    }

    @Override
    public CUGCheckInfo getCUGCheckInfo() {
        return this.cugCheckInfo;
    }

    @Override
    public boolean getCUGSubscriptionFlag() {
        return false;
    }

    @Override
    public SubscriberInfo getSubscriberInfo() {
        return null;
    }

    @Override
    public List<SSCode> getSSList() {
        return null;
    }

    @Override
    public ExtBasicServiceCode getBasicService() {
        return null;
    }

    @Override
    public boolean getForwardingInterrogationRequired() {
        return false;
    }

    @Override
    public ISDNAddressString getVmscAddress() {
        return null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    @Override
    public NAEAPreferredCI getNaeaPreferredCI() {
        return null;
    }

    @Override
    public CCBSIndicators getCCBSIndicators() {
        return null;
    }

    @Override
    public ISDNAddressString getMsisdn() {
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
    public SupportedCamelPhases getSupportedCamelPhasesInVMSC() {
        return null;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIsInVMSC() {
        return null;
    }

    @Override
    public RoutingInfo getRoutingInfo2() {
        return routingInfo;
    }

    @Override
    public List<SSCode> getSSList2() {
        return null;
    }

    @Override
    public ExtBasicServiceCode getBasicService2() {
        return null;
    }

    @Override
    public AllowedServices getAllowedServices() {
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
    public ExternalSignalInfo getGsmBearerCapability() {
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