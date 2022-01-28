/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RetrieveResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DataItemInformationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
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
    		this.operationReturnID=new ASNOctetString(operationReturnID);    	
    	
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
}
