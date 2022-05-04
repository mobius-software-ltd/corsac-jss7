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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.HandOverInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCPAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCPDialogueInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

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
    		ByteBuf sendingSCPCorrelationInfo, SCPAddress receivingSCPAddress, SCPDialogueInfo receivingSCPDialogueInfo,
    		ByteBuf receivingSCPCorrelationInfo,CalledPartyNumberIsup handoverNumber,Integer handoverData) {
    	if(handoverCounter!=null)
    		this.handoverCounter=new ASNInteger(handoverCounter,"HandoverCounter",1,127,false);
    		
    	if(sendingSCPAddress!=null)
    		this.sendingSCPAddress=new SCPAddressWrapperImpl(sendingSCPAddress);
    	
    	this.sendingSCPDialogueInfo=sendingSCPDialogueInfo;
    	
    	if(sendingSCPCorrelationInfo!=null)
    		this.sendingSCPCorrelationInfo=new ASNOctetString(sendingSCPCorrelationInfo,"SendingSCPCorrelationInfo",16,16,false);    		
    	
    	if(receivingSCPAddress!=null)
    		this.receivingSCPAddress=new SCPAddressWrapperImpl(receivingSCPAddress);
    	
    	this.receivingSCPDialogueInfo=receivingSCPDialogueInfo;
    	if(receivingSCPCorrelationInfo!=null)
    		this.receivingSCPCorrelationInfo=new ASNOctetString(receivingSCPCorrelationInfo,"SendingSCPCorrelationInfo",16,16,false);
    		
    	this.handoverNumber=handoverNumber;
    	if(handoverData!=null)
    		this.handoverData=new ASNInteger(handoverData,"HandoverData",0,65535,false);    		
    }       

    public Integer getHandoverCounter() {
    	if(handoverCounter==null)
    		return null;
    	
    	return handoverCounter.getIntValue();
    }      

    public SCPAddress getSendingSCPAddress() {
    	if(sendingSCPAddress==null)
    		return null;
    	
    	return sendingSCPAddress.getSCPAddress();
    }     

    public SCPDialogueInfo getSendingSCPDialogueInfo() {
    	return sendingSCPDialogueInfo;
    }      

    public ByteBuf getSendingSCPCorrelationInfo() {
    	if(sendingSCPCorrelationInfo==null)
    		return null;
    	
    	return sendingSCPCorrelationInfo.getValue();
    }      

    public SCPAddress getReceivingSCPAddress() {
    	if(receivingSCPAddress==null)
    		return null;
    	
    	return receivingSCPAddress.getSCPAddress();
    }     

    public SCPDialogueInfo getReceivingSCPDialogueInfo() {
    	return receivingSCPDialogueInfo;
    }      

    public ByteBuf getReceivingSCPCorrelationInfo() {
    	if(receivingSCPCorrelationInfo==null)
    		return null;
    	
    	return receivingSCPCorrelationInfo.getValue();
    }  

    public CalledPartyNumberIsup getHandoverNumber() {
    	return handoverNumber;
    }
    
    public Integer getHandoverData() {
    	if(handoverData==null)
    		return null;
    	
    	return handoverData.getIntValue();
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
            sb.append(sendingSCPCorrelationInfo.printDataArr());
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
            sb.append(receivingSCPCorrelationInfo.printDataArr());
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(handoverCounter==null)
			throw new ASNParsingComponentException("handover counter should be set for handover info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(sendingSCPAddress==null)
			throw new ASNParsingComponentException("sending scp address should be set for handover info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(sendingSCPDialogueInfo==null)
			throw new ASNParsingComponentException("sending scp dialogue info should be set for handover info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(receivingSCPAddress==null)
			throw new ASNParsingComponentException("receiving scp address should be set for handover info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(receivingSCPDialogueInfo==null)
			throw new ASNParsingComponentException("receiving scp dialogue info should be set for handover info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}