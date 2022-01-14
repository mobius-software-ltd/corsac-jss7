/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1Impl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ApplyChargingRequestCS1Impl extends CircuitSwitchedCallMessageImpl implements ApplyChargingRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = AchBillingChargingCharacteristicsCS1Impl.class)
    private AChBillingChargingCharacteristics aChBillingChargingCharacteristics;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNBoolean sendCalculationToSCPIndication;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private LegIDWrapperImpl partyToCharge;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public ApplyChargingRequestCS1Impl() {
    }

    public ApplyChargingRequestCS1Impl(AchBillingChargingCharacteristicsCS1 aChBillingChargingCharacteristics,
    		Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions) {
        this.aChBillingChargingCharacteristics = aChBillingChargingCharacteristics;
        
        if(sendCalculationToSCPIndication!=null) {
        	this.sendCalculationToSCPIndication=new ASNBoolean();
        	this.sendCalculationToSCPIndication.setValue(sendCalculationToSCPIndication);
        }
        
        if(partyToCharge!=null)
        	this.partyToCharge = new LegIDWrapperImpl(partyToCharge);
        
        this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.applyCharging_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.applyCharging;
    }

    @Override
    public AChBillingChargingCharacteristics getAChBillingChargingCharacteristics() {
        return aChBillingChargingCharacteristics;
    }

    @Override
    public Boolean getSendCalculationToSCPIndication() {
    	if(sendCalculationToSCPIndication==null)
    		return null;
    	
        return sendCalculationToSCPIndication.getValue();
    }

    @Override
    public LegID getPartyToCharge() {
    	if(partyToCharge==null || partyToCharge.getLegID()==null)
    		return null;
    	
        return partyToCharge.getLegID();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ApplyChargingRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.aChBillingChargingCharacteristics != null) {
            sb.append(", aChBillingChargingCharacteristics=");
            sb.append(aChBillingChargingCharacteristics.toString());
        }
        
        if (this.sendCalculationToSCPIndication != null && sendCalculationToSCPIndication.getValue()!=null) {
            sb.append(", sendCalculationToSCPIndication=");
            sb.append(sendCalculationToSCPIndication.getValue());
        }
        
        if (this.partyToCharge != null && this.partyToCharge.getLegID()!=null) {
            sb.append(", partyToCharge=");
            sb.append(partyToCharge.getLegID());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
