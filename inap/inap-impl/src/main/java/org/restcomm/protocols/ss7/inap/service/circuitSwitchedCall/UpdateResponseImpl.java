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

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.UpdateResponse;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.UpdateResultChoiseImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

import io.netty.buffer.ByteBuf;

/**
 *
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class UpdateResponseImpl extends CircuitSwitchedCallMessageImpl implements UpdateResponse {
	private static final long serialVersionUID = 1L;

	@ASNChoise	
    private UpdateResultChoiseImpl updateResponseChoise;

    public UpdateResponseImpl() {
    }

    public UpdateResponseImpl(ByteBuf operationReturnID) {
        this.updateResponseChoise = new UpdateResultChoiseImpl(operationReturnID);
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.update_Response;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.update;
    }

    @Override
    public ByteBuf getOperationReturnID() {
    	if(updateResponseChoise==null)
        	return null;
    	
    	return updateResponseChoise.getOperationReturnID();
    }

    @Override
    public String toString() {
        if (this.updateResponseChoise != null)
            return this.updateResponseChoise.toString();
        
        return "";
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(updateResponseChoise==null)
			throw new ASNParsingComponentException("one of child items should be set for update response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
