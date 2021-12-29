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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.UpdateRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ApplicationID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ApplicationIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DataItemIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DataItemInformationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class UpdateRequestImpl extends CircuitSwitchedCallMessageImpl implements UpdateRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNOctetString operationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = ApplicationIDImpl.class)
    private ApplicationID applicationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = DataItemIDImpl.class)
    private DataItemID dataItemID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = DataItemInformationImpl.class)
    private DataItemInformation dataItemInformation;
    
	public UpdateRequestImpl() {
    }

    public UpdateRequestImpl(byte[] operationID,ApplicationID applicationID,DataItemID dataItemID,DataItemInformation dataItemInformation) {
    	if(operationID!=null) {
    		this.operationID=new ASNOctetString();
    		this.operationID.setValue(Unpooled.wrappedBuffer(operationID));
    	}
    	
    	this.applicationID=applicationID;
    	this.dataItemID=dataItemID;
    	this.dataItemInformation=dataItemInformation;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.update_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.update;
    }
    
    @Override
    public byte[] getOperationID() 
    {
    	if(operationID==null || operationID.getValue()==null)
    		return null;
    	
    	ByteBuf value=operationID.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
		return data;
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
    public DataItemInformation getDataItemInformation() {
		return dataItemInformation;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RetrieveRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.operationID != null && this.operationID.getValue()!=null) {
            sb.append(", operationID=");
            sb.append(ASNOctetString.printDataArr(getOperationID()));
        }
        if (this.applicationID != null) {
            sb.append(", applicationID=");
            sb.append(applicationID.toString());
        }
        if (this.dataItemID != null) {
            sb.append(", dataItemID=");
            sb.append(dataItemID);
        }
        if (this.dataItemInformation != null) {
            sb.append(", dataItemInformation=");
            sb.append(dataItemInformation);
        }
        sb.append("]");

        return sb.toString();
    }
}
