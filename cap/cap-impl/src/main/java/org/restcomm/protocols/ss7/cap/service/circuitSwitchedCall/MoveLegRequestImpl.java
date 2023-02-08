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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
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
public class MoveLegRequestImpl extends CircuitSwitchedCallMessageImpl implements MoveLegRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private LegIDWrapperImpl legIDToMove;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public MoveLegRequestImpl() {
    }

    public MoveLegRequestImpl(LegID legIDToMove, CAPINAPExtensions extensions) {
    	
    	if(legIDToMove!=null)
    		this.legIDToMove = new LegIDWrapperImpl(legIDToMove);
    	
        this.extensions = extensions;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.moveLeg_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.moveLeg;
    }

    @Override
    public LegID getLegIDToMove() {
    	if(legIDToMove==null)
    		return null;
    	
        return legIDToMove.getLegID();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MoveLegRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legIDToMove != null && this.legIDToMove.getLegID()!=null) {
            sb.append(", legIDToMove=");
            sb.append(legIDToMove.getLegID());
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
		if(legIDToMove==null)
			throw new ASNParsingComponentException("leg ID to move should be set for move leg request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
