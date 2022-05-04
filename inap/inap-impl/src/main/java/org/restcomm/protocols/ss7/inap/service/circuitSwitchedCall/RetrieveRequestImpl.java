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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RetrieveRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ApplicationID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemID;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ApplicationIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DataItemIDImpl;

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
public class RetrieveRequestImpl extends CircuitSwitchedCallMessageImpl implements RetrieveRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNOctetString operationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = ApplicationIDImpl.class)
    private ApplicationID applicationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = DataItemIDImpl.class)
    private DataItemID dataItemID;
    
	public RetrieveRequestImpl() {
    }

    public RetrieveRequestImpl(ByteBuf operationID,ApplicationID applicationID,DataItemID dataItemID) {
    	if(operationID!=null)
    		this.operationID=new ASNOctetString(operationID,"OperationID",null,null,false);    	
    	
    	this.applicationID=applicationID;
    	this.dataItemID=dataItemID;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.retrieve_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.retrieve;
    }
    
    @Override
    public ByteBuf getOperationID() 
    {
    	if(operationID==null)
    		return null;
    	
    	return operationID.getValue();
	}

    @Override
    public ApplicationID getApplicationID() {
		return applicationID;
	}

    @Override
    public DataItemID getDataItemID() {
		return dataItemID;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RetrieveRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.operationID != null) {
            sb.append(", operationID=");
            sb.append(operationID.printDataArr());
        }
        if (this.applicationID != null) {
            sb.append(", applicationID=");
            sb.append(applicationID.toString());
        }
        if (this.dataItemID != null) {
            sb.append(", dataItemID=");
            sb.append(dataItemID);
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(operationID==null)
			throw new ASNParsingComponentException("operation ID should be set for retrieve request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(applicationID==null)
			throw new ASNParsingComponentException("application ID should be set for retrieve request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(dataItemID==null)
			throw new ASNParsingComponentException("data item ID should be set for retrieve request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
