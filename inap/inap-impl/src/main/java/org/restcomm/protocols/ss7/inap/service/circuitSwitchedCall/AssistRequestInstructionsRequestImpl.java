/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.IPAvailableImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AssistRequestInstructionsRequestImpl extends CircuitSwitchedCallMessageImpl implements
        AssistRequestInstructionsRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
	private DigitsIsup correlationID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = IPAvailableImpl.class)
	private IPAvailable ipAvailable;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = IPSSPCapabilitiesImpl.class)
	private IPSSPCapabilities iPSSPCapabilities;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;

    public AssistRequestInstructionsRequestImpl() {
    }

    public AssistRequestInstructionsRequestImpl(DigitsIsup correlationID, IPAvailable ipAvailable,
    		IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) {
        this.correlationID = correlationID;
        this.ipAvailable = ipAvailable;
        this.iPSSPCapabilities = ipSSPCapabilities;
        this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.assistRequestInstructions_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.assistRequestInstructions;
    }

    @Override
    public DigitsIsup getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericNumber();
    	
        return correlationID;
    }

    @Override
    public IPAvailable getIPAvailable() {
        return ipAvailable;
    }

    @Override
    public IPSSPCapabilities getIPSSPCapabilities() {
        return iPSSPCapabilities;
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AssistRequestInstructionsRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID.toString());
        }
        if (this.ipAvailable != null) {
            sb.append(", ipAvailable=");
            sb.append(ipAvailable.toString());
        }
        if (this.iPSSPCapabilities != null) {
            sb.append(", iPSSPCapabilities=");
            sb.append(iPSSPCapabilities.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(correlationID==null)
			throw new ASNParsingComponentException("correlation ID should be set for assist request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
