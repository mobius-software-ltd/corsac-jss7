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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AChChargingAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAMELAChBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AChChargingAddressWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ApplyChargingRequestImpl extends CircuitSwitchedCallMessageImpl implements ApplyChargingRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = CAMELAChBillingChargingCharacteristicsImpl.class)
    private CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private SendingLegIDWrapperImpl partyToCharge;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = true,index = -1)
    private AChChargingAddressWrapperImpl aChChargingAddress;

    public ApplyChargingRequestImpl() {
    }

    public ApplyChargingRequestImpl(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) {
        this.aChBillingChargingCharacteristics = aChBillingChargingCharacteristics;
        
        if(partyToCharge!=null)
        	this.partyToCharge = new SendingLegIDWrapperImpl(new SendingLegIDImpl(partyToCharge));
        
        this.extensions = extensions;
        
        if(aChChargingAddress!=null)
        	this.aChChargingAddress = new AChChargingAddressWrapperImpl(aChChargingAddress);
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.applyCharging_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.applyCharging;
    }

    @Override
    public CAMELAChBillingChargingCharacteristics getAChBillingChargingCharacteristics() {
        return aChBillingChargingCharacteristics;
    }

    @Override
    public LegType getPartyToCharge() {
    	if(partyToCharge==null || partyToCharge.getSendingLegID()==null)
    		return null;
    	
        return partyToCharge.getSendingLegID().getSendingSideID();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public AChChargingAddress getAChChargingAddress() {
    	if(aChChargingAddress==null)
    		return null;
    	
        return aChChargingAddress.getAChChargingAddress();
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
        if (this.partyToCharge != null && this.partyToCharge.getSendingLegID()!=null) {
            sb.append(", partyToCharge=");
            sb.append(partyToCharge.getSendingLegID());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.aChChargingAddress != null && this.aChChargingAddress.getAChChargingAddress()!=null) {
            sb.append(", aChChargingAddress=");
            sb.append(aChChargingAddress.getAChChargingAddress());
        }

        sb.append("]");

        return sb.toString();
    }
}
