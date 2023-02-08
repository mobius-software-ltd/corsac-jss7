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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Povilas Jurna
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DisconnectLegRequestImpl extends CircuitSwitchedCallMessageImpl implements DisconnectLegRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private LegIDWrapperImpl legToBeReleased;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = CauseIsupImpl.class)
    private CauseIsup releaseCause;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public DisconnectLegRequestImpl() {
    }

    public DisconnectLegRequestImpl(LegID legToBeReleased, CauseIsup releaseCause, CAPINAPExtensions extensions) {
    	
    	if(legToBeReleased!=null)
    		this.legToBeReleased = new LegIDWrapperImpl(legToBeReleased);
    	
        this.releaseCause = releaseCause;
        this.extensions = extensions;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.disconnectLeg_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.disconnectLeg;
    }

    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    public LegID getLegToBeReleased() {
    	if(legToBeReleased==null)
    		return null;
    	
        return legToBeReleased.getLegID();
    }

    public CauseIsup getReleaseCause() {
        return releaseCause;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DisconnectlegRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legToBeReleased != null) {
            sb.append(", legToBeReleased=");
            sb.append(legToBeReleased.toString());
        }
        if (this.releaseCause != null) {
            sb.append(", releaseCause=");
            sb.append(releaseCause.toString());
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
		if(legToBeReleased==null)
			throw new ASNParsingComponentException("leg to be released should be set for disconnect leg request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
