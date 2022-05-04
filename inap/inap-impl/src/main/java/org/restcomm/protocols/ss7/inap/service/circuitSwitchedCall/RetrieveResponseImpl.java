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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RetrieveResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DataItemInformationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class RetrieveResponseImpl extends CircuitSwitchedCallMessageImpl implements RetrieveResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNOctetString operationReturnID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = DataItemInformationImpl.class)
    private DataItemInformation dataItemInformation;
    
	public RetrieveResponseImpl() {
    }

    public RetrieveResponseImpl(ByteBuf operationReturnID,DataItemInformation dataItemInformation) {
    	if(operationReturnID!=null)
    		this.operationReturnID=new ASNOctetString(operationReturnID,"OperationID",null,null,false);    	
    	
    	this.dataItemInformation=dataItemInformation;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.retrieve_Response;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.retrieve;
    }
    
    @Override
    public ByteBuf getOperationReturnID() 
    {
    	if(operationReturnID==null)
    		return null;
    	
    	return operationReturnID.getValue();
	}

    @Override
    public DataItemInformation getDataItemInformation() {
		return dataItemInformation;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RetrieveResponseIndication [");
        this.addInvokeIdInfo(sb);

        if (this.operationReturnID != null && this.operationReturnID.getValue()!=null) {
            sb.append(", operationReturnID=");
            sb.append(operationReturnID.printDataArr());
        }
        if (this.dataItemInformation != null) {
            sb.append(", dataItemInformation=");
            sb.append(dataItemInformation);
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(operationReturnID==null)
			throw new ASNParsingComponentException("operation return ID should be set for retrieve response", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(dataItemInformation==null)
			throw new ASNParsingComponentException("data item information should be set for retrieve response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
