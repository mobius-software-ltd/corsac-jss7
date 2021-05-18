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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.primitives.AChChargingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.AChChargingAddressWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.ReceivingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TimeDurationChargingResultImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private ReceivingLegIDWrapperImpl partyToCharge;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private TimeInformationWrapperImpl timeInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNBoolean legActive;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNNull callLegReleasedAtTcpExpiry;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1)
    private AChChargingAddressWrapperImpl aChChargingAddress;

    public TimeDurationChargingResultImpl() {
    }

    public TimeDurationChargingResultImpl(ReceivingLegIDImpl partyToCharge, TimeInformationImpl timeInformation, boolean legActive,
            boolean callLegReleasedAtTcpExpiry, CAPExtensionsImpl extensions, AChChargingAddressImpl aChChargingAddress) {
        if(partyToCharge!=null)
        	this.partyToCharge = new ReceivingLegIDWrapperImpl(partyToCharge);
        
        if(timeInformation!=null)
        	this.timeInformation = new TimeInformationWrapperImpl(timeInformation);
        
        this.legActive = new ASNBoolean();
        this.legActive.setValue(legActive);
        
        if(callLegReleasedAtTcpExpiry)
        	this.callLegReleasedAtTcpExpiry = new ASNNull();
        
        this.extensions = extensions;
        
        if(aChChargingAddress!=null)
        	this.aChChargingAddress = new AChChargingAddressWrapperImpl(aChChargingAddress);
    }

    public ReceivingLegIDImpl getPartyToCharge() {
    	if(partyToCharge==null)
    		return null;
    	
        return partyToCharge.getReceivingLegID();
    }

    public TimeInformationImpl getTimeInformation() {
    	if(timeInformation==null)
    		return null;
    	
        return timeInformation.getTimeInformation();
    }

    public boolean getLegActive() {
    	if(legActive==null || legActive.getValue()==null)
    		return true;
    	
        return legActive.getValue();
    }

    public boolean getCallLegReleasedAtTcpExpiry() {
        return callLegReleasedAtTcpExpiry!=null;
    }

    public CAPExtensionsImpl getExtensions() {
        return extensions;
    }

    public AChChargingAddressImpl getAChChargingAddress() {
    	if(aChChargingAddress==null)
    		return null;
    	
        return aChChargingAddress.getAChChargingAddress();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TimeDurationChargingResult [");

        if (this.partyToCharge != null && this.partyToCharge.getReceivingLegID()!=null) {
            sb.append("partyToCharge=");
            sb.append(partyToCharge.getReceivingLegID());
        }
        if (this.timeInformation != null && this.timeInformation.getTimeInformation()!=null) {
            sb.append(", timeInformation=");
            sb.append(timeInformation.getTimeInformation());
        }
        if (this.legActive==null || this.legActive.getValue()==null || this.legActive.getValue()) {
            sb.append(", legActive");
        }
        if (this.callLegReleasedAtTcpExpiry!=null) {
            sb.append(", callLegReleasedAtTcpExpiry");
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.aChChargingAddress != null) {
            sb.append(", aChChargingAddress=");
            sb.append(aChChargingAddress.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
