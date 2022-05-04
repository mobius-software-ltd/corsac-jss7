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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Povilas Jurna
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitiateCallAttemptRequestImpl extends CircuitSwitchedCallMessageImpl implements
        InitiateCallAttemptRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = DestinationRoutingAddressImpl.class)
    private DestinationRoutingAddress destinationRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1)
    private LegIDWrapperImpl legToBeCreated;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private ASNInteger newCallSegment;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1, defaultImplementation = CallReferenceNumberImpl.class)
    private CallReferenceNumber callReferenceNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gsmSCFAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 53,constructed = false,index = -1)
    private ASNNull suppressTCsi;

    public InitiateCallAttemptRequestImpl() {
    }

    public InitiateCallAttemptRequestImpl(DestinationRoutingAddress destinationRoutingAddress,
            CAPINAPExtensions extensions, LegID legToBeCreated, Integer newCallSegment,
            CallingPartyNumberIsup callingPartyNumber, CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        this.extensions = extensions;
        
        if(legToBeCreated!=null)
        	this.legToBeCreated = new LegIDWrapperImpl(legToBeCreated);
        
        if(newCallSegment!=null)
        	this.newCallSegment = new ASNInteger(newCallSegment,"CallSegmentID",0,127,false);
        	
        this.callingPartyNumber = callingPartyNumber;
        this.callReferenceNumber = callReferenceNumber;
        this.gsmSCFAddress = gsmSCFAddress;
        
        if(suppressTCsi)
        	this.suppressTCsi = new ASNNull();
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.initiateCallAttempt_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.initiateCallAttempt;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("InitiateCallAttemptIndication [");
        this.addInvokeIdInfo(sb);

        if (this.destinationRoutingAddress != null) {
            sb.append(", destinationRoutingAddress=");
            sb.append(destinationRoutingAddress.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.legToBeCreated != null && this.legToBeCreated.getLegID()!=null) {
            sb.append("legToBeCreated=");
            sb.append(legToBeCreated.getLegID());
        }
        if (this.newCallSegment != null && this.newCallSegment.getValue()!=null) {
            sb.append(", newCallSegment=");
            sb.append(newCallSegment.getValue());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber);
        }
        if (this.callReferenceNumber != null) {
            sb.append(", callReferenceNumber=");
            sb.append(callReferenceNumber);
        }
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(gsmSCFAddress);
        }
        if (this.suppressTCsi!=null) {
            sb.append(", suppressTCsi=");
            sb.append(suppressTCsi);
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public LegID getLegToBeCreated() {    	
    	if(legToBeCreated==null)
    		return null;
    	
        return legToBeCreated.getLegID();
    }

    @Override
    public Integer getNewCallSegment() {
    	if(newCallSegment==null)
    		return null;
    	
        return newCallSegment.getIntValue();
    }

    @Override
    public CallingPartyNumberIsup getCallingPartyNumber() {
        return callingPartyNumber;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    @Override
    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    @Override
    public boolean getSuppressTCsi() {
        return suppressTCsi!=null;
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(destinationRoutingAddress==null)
			throw new ASNParsingComponentException("destination routing address should be set for initiate call attempt request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
