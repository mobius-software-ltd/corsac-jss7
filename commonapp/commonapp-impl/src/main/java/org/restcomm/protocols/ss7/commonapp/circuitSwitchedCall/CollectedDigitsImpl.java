/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedDigits;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNErrorTreatment;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CollectedDigitsImpl implements CollectedDigits {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger minimumNbOfDigits;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger maximumNbOfDigits;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNOctetString endOfReplyDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNOctetString cancelDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private ASNOctetString startDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
    private ASNInteger firstDigitTimeOut;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private ASNInteger interDigitTimeOut;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1)
    private ASNErrorTreatment errorTreatment;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1)
    private ASNBoolean interruptableAnnInd;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1)
    private ASNBoolean voiceInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1)
    private ASNBoolean voiceBack;

    public CollectedDigitsImpl() {
    }

    public CollectedDigitsImpl(Integer minimumNbOfDigits, int maximumNbOfDigits, ByteBuf endOfReplyDigit, ByteBuf cancelDigit,
    		ByteBuf startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment,
            Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack) {
        if(minimumNbOfDigits!=null)
        	this.minimumNbOfDigits = new ASNInteger(minimumNbOfDigits,"MinimumNbOfDigits",1,30,false);
        	
        this.maximumNbOfDigits = new ASNInteger(maximumNbOfDigits,"MaximumNbOfDigits",1,30,false);
        
        if(endOfReplyDigit!=null)
        	this.endOfReplyDigit = new ASNOctetString(endOfReplyDigit,"EndOfReplyDigit",1,2,false);        
        
        if(cancelDigit!=null)
        	this.cancelDigit = new ASNOctetString(cancelDigit,"EndOfReplyDigit",1,2,false);        
        
        if(startDigit!=null)
        	this.startDigit = new ASNOctetString(startDigit,"EndOfReplyDigit",1,2,false);        
        
        if(firstDigitTimeOut!=null)
        	this.firstDigitTimeOut = new ASNInteger(firstDigitTimeOut,"FirstDigitTimeOut",1,127,false);
        	
        if(interDigitTimeOut!=null)
        	this.interDigitTimeOut = new ASNInteger(interDigitTimeOut,"InterDigitTimeOut",1,127,false);
        	
        if(errorTreatment!=null)
        	this.errorTreatment = new ASNErrorTreatment(errorTreatment);
        	
        if(interruptableAnnInd!=null)
        	this.interruptableAnnInd = new ASNBoolean(interruptableAnnInd,"InterruptableAnnInd",true,false);        	
        
        if(voiceInformation!=null)
        	this.voiceInformation = new ASNBoolean(voiceInformation,"VoiceInformation",true,false);        	
        
        if(voiceBack!=null)
        	this.voiceBack = new ASNBoolean(voiceBack,"VoiceBack",true,false);        	
    }

    public Integer getMinimumNbOfDigits() {
    	if(minimumNbOfDigits==null)
    		return null;
    	
        return minimumNbOfDigits.getIntValue();
    }

    public int getMaximumNbOfDigits() {
    	if(maximumNbOfDigits==null || maximumNbOfDigits.getValue()==null)
    		return 0;
    	
        return maximumNbOfDigits.getIntValue();
    }

    public ByteBuf getEndOfReplyDigit() {
    	if(endOfReplyDigit==null)
    		return null;
    	
    	return endOfReplyDigit.getValue();    	
    }

    public ByteBuf getCancelDigit() {
    	if(cancelDigit==null)
    		return null;
    	
    	return cancelDigit.getValue();    	
    }

    public ByteBuf getStartDigit() {
    	if(startDigit==null)
    		return null;
    	
    	return startDigit.getValue();
    }

    public Integer getFirstDigitTimeOut() {
    	if(firstDigitTimeOut==null)
    		return null;
    	
        return firstDigitTimeOut.getIntValue();
    }

    public Integer getInterDigitTimeOut() {
    	if(interDigitTimeOut==null)
    		return null;
    	
        return interDigitTimeOut.getIntValue();
    }

    public ErrorTreatment getErrorTreatment() {
    	if(errorTreatment==null)
    		return null;
    	
        return errorTreatment.getType();
    }

    public Boolean getInterruptableAnnInd() {
    	if(interruptableAnnInd==null || interruptableAnnInd.getValue()==null)
    		return true;
    	
        return interruptableAnnInd.getValue();
    }

    public Boolean getVoiceInformation() {
    	if(voiceInformation==null || voiceInformation.getValue()==null)
    		return false;
    	
        return voiceInformation.getValue();
    }

    public Boolean getVoiceBack() {
    	if(voiceBack==null || voiceBack.getValue()==null)
    		return false;
    	
        return voiceBack.getValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CollectedDigits [");

        if (this.minimumNbOfDigits != null) {
            sb.append("minimumNbOfDigits=");
            sb.append(this.minimumNbOfDigits);
        }
        sb.append(", maximumNbOfDigits=");
        sb.append(this.maximumNbOfDigits);
        if (this.endOfReplyDigit != null) {
            sb.append(", endOfReplyDigit=[");
            sb.append(endOfReplyDigit.printDataArr());
            sb.append("]");
        }
        if (this.cancelDigit != null) {
            sb.append(", cancelDigit=[");
            sb.append(cancelDigit.printDataArr());
            sb.append("]");
        }
        if (this.startDigit != null) {
            sb.append(", startDigit=[");
            sb.append(startDigit.printDataArr());
            sb.append("]");
        }
        if (this.firstDigitTimeOut != null) {
            sb.append(", firstDigitTimeOut=");
            sb.append(this.firstDigitTimeOut.getValue());
        }
        if (this.interDigitTimeOut != null) {
            sb.append(", interDigitTimeOut=");
            sb.append(this.interDigitTimeOut.getValue());
        }
        if (this.errorTreatment != null && this.errorTreatment.getType() != null) {
            sb.append(", errorTreatment=");
            sb.append(this.errorTreatment.toString());
        }
        if (this.interruptableAnnInd != null) {
            sb.append(", interruptableAnnInd=");
            sb.append(this.interruptableAnnInd.getValue());
        }
        if (this.voiceInformation != null) {
            sb.append(", voiceInformation=");
            sb.append(this.voiceInformation.getValue());
        }
        if (this.voiceBack != null) {
            sb.append(", voiceBack=");
            sb.append(this.voiceBack.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}