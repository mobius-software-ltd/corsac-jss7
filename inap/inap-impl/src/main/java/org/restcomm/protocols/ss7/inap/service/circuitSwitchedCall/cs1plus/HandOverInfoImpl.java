/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.HandOverInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCPAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCPDialogueInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class HandOverInfoImpl implements HandOverInfo {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger handoverCounter;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1)
    private SCPAddressWrapperImpl sendingSCPAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true, index=-1, defaultImplementation = SCPDialogueInfoImpl.class)
    private SCPDialogueInfo sendingSCPDialogueInfo;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNOctetString sendingSCPCorrelationInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true, index=-1)
    private SCPAddressWrapperImpl receivingSCPAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true, index=-1, defaultImplementation = SCPDialogueInfoImpl.class)
    private SCPDialogueInfo receivingSCPDialogueInfo;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false, index=-1)
    private ASNOctetString receivingSCPCorrelationInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false, index=-1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup handoverNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false, index=-1)
    private ASNInteger handoverData;

    public HandOverInfoImpl() {
    }

    public HandOverInfoImpl(Integer handoverCounter,SCPAddress sendingSCPAddress,SCPDialogueInfo sendingSCPDialogueInfo,
    		byte[] sendingSCPCorrelationInfo, SCPAddress receivingSCPAddress, SCPDialogueInfo receivingSCPDialogueInfo,
    		byte[] receivingSCPCorrelationInfo,CalledPartyNumberIsup handoverNumber,Integer handoverData) {
    	if(handoverCounter!=null) {
    		this.handoverCounter=new ASNInteger();
    		this.handoverCounter.setValue(handoverCounter.longValue());
    	}
    	
    	if(sendingSCPAddress!=null)
    		this.sendingSCPAddress=new SCPAddressWrapperImpl(sendingSCPAddress);
    	
    	this.sendingSCPDialogueInfo=sendingSCPDialogueInfo;
    	if(sendingSCPCorrelationInfo!=null) {
    		this.sendingSCPCorrelationInfo=new ASNOctetString();
    		this.sendingSCPCorrelationInfo.setValue(Unpooled.wrappedBuffer(sendingSCPCorrelationInfo));
    	}
    	
    	if(receivingSCPAddress!=null)
    		this.receivingSCPAddress=new SCPAddressWrapperImpl(receivingSCPAddress);
    	
    	this.receivingSCPDialogueInfo=receivingSCPDialogueInfo;
    	if(receivingSCPCorrelationInfo!=null) {
    		this.receivingSCPCorrelationInfo=new ASNOctetString();
    		this.receivingSCPCorrelationInfo.setValue(Unpooled.wrappedBuffer(receivingSCPCorrelationInfo));
    	}
    	
    	this.handoverNumber=handoverNumber;
    	if(handoverData!=null) {
    		this.handoverData=new ASNInteger();
    		this.handoverData.setValue(handoverData.longValue());
    	}
    }       

    public Integer getHandoverCounter() {
    	if(handoverCounter==null || handoverCounter.getValue()==null)
    		return null;
    	
    	return handoverCounter.getValue().intValue();
    }      

    public SCPAddress getSendingSCPAddress() {
    	if(sendingSCPAddress==null)
    		return null;
    	
    	return sendingSCPAddress.getSCPAddress();
    }     

    public SCPDialogueInfo getSendingSCPDialogueInfo() {
    	return sendingSCPDialogueInfo;
    }      

    public byte[] getSendingSCPCorrelationInfo() {
    	if(sendingSCPCorrelationInfo==null || sendingSCPCorrelationInfo.getValue()==null)
    		return null;
    	
    	ByteBuf value=sendingSCPCorrelationInfo.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }      

    public SCPAddress getReceivingSCPAddress() {
    	if(receivingSCPAddress==null)
    		return null;
    	
    	return receivingSCPAddress.getSCPAddress();
    }     

    public SCPDialogueInfo getReceivingSCPDialogueInfo() {
    	return receivingSCPDialogueInfo;
    }      

    public byte[] getReceivingSCPCorrelationInfo() {
    	if(receivingSCPCorrelationInfo==null || receivingSCPCorrelationInfo.getValue()==null)
    		return null;
    	
    	ByteBuf value=receivingSCPCorrelationInfo.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }  

    public CalledPartyNumberIsup getHandoverNumber() {
    	return handoverNumber;
    }
    
    public Integer getHandoverData() {
    	if(handoverData==null || handoverData.getValue()==null)
    		return null;
    	
    	return handoverData.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("HandOverInfo [");

        if (this.handoverCounter != null && this.handoverCounter.getValue()!=null) {
            sb.append(", handoverCounter=");
            sb.append(handoverCounter.getValue());
        }
        
        if (this.sendingSCPAddress != null && this.sendingSCPAddress.getSCPAddress()!=null) {
            sb.append(", sendingSCPAddress=");
            sb.append(sendingSCPAddress.getSCPAddress());
        }
        
        if (this.sendingSCPDialogueInfo != null) {
            sb.append(", sendingSCPDialogueInfo=");
            sb.append(sendingSCPDialogueInfo);
        }
        
        if (this.sendingSCPCorrelationInfo != null && this.sendingSCPCorrelationInfo.getValue()!=null) {
            sb.append(", sendingSCPCorrelationInfo=");
            sb.append(ASNOctetString.printDataArr(getSendingSCPCorrelationInfo()));
        }
        
        if (this.receivingSCPAddress != null && this.receivingSCPAddress.getSCPAddress()!=null) {
            sb.append(", receivingSCPAddress=");
            sb.append(receivingSCPAddress.getSCPAddress());
        }
        
        if (this.receivingSCPDialogueInfo != null) {
            sb.append(", receivingSCPDialogueInfo=");
            sb.append(receivingSCPDialogueInfo);
        }
        
        if (this.receivingSCPCorrelationInfo != null && this.receivingSCPCorrelationInfo.getValue()!=null) {
            sb.append(", receivingSCPCorrelationInfo=");
            sb.append(ASNOctetString.printDataArr(getReceivingSCPCorrelationInfo()));
        }
        
        if (this.handoverNumber != null) {
            sb.append(", handoverNumber=");
            sb.append(handoverNumber);
        }
        
        if (this.handoverData != null && this.handoverData.getValue()!=null) {
            sb.append(", handoverData=");
            sb.append(handoverData.getValue());
        }
        
        sb.append("]");

        return sb.toString();
    }
}