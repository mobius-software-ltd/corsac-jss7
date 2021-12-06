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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.primitives.ErrorTreatment;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigits;
import org.restcomm.protocols.ss7.cap.primitives.ASNErrorTreatmentImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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
    private ASNErrorTreatmentImpl errorTreatment;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1)
    private ASNBoolean interruptableAnnInd;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1)
    private ASNBoolean voiceInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1)
    private ASNBoolean voiceBack;

    public CollectedDigitsImpl() {
    }

    public CollectedDigitsImpl(Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit,
            byte[] startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment,
            Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack) {
        if(minimumNbOfDigits!=null) {
        	this.minimumNbOfDigits = new ASNInteger();
        	this.minimumNbOfDigits.setValue(minimumNbOfDigits.longValue());
        }
        
        this.maximumNbOfDigits = new ASNInteger();
        this.maximumNbOfDigits.setValue(Long.valueOf(maximumNbOfDigits));
        
        if(endOfReplyDigit!=null) {
        	this.endOfReplyDigit = new ASNOctetString();
        	this.endOfReplyDigit.setValue(Unpooled.wrappedBuffer(endOfReplyDigit));
        }
        
        if(cancelDigit!=null) {
        	this.cancelDigit = new ASNOctetString();
        	this.cancelDigit.setValue(Unpooled.wrappedBuffer(cancelDigit));
        }
        
        if(startDigit!=null) {
        	this.startDigit = new ASNOctetString();
        	this.startDigit.setValue(Unpooled.wrappedBuffer(startDigit));
        }
        
        if(firstDigitTimeOut!=null) {
        	this.firstDigitTimeOut = new ASNInteger();
        	this.firstDigitTimeOut.setValue(firstDigitTimeOut.longValue());
        }
        
        if(interDigitTimeOut!=null) {
        	this.interDigitTimeOut = new ASNInteger();
        	this.interDigitTimeOut.setValue(interDigitTimeOut.longValue());
        }
        
        if(errorTreatment!=null) {
        	this.errorTreatment = new ASNErrorTreatmentImpl();
        	this.errorTreatment.setType(errorTreatment);
        }
        
        if(interruptableAnnInd!=null) {
        	this.interruptableAnnInd = new ASNBoolean();
        	this.interruptableAnnInd.setValue(interruptableAnnInd);
        }
        
        if(voiceInformation!=null) {
        	this.voiceInformation = new ASNBoolean();
        	this.voiceInformation.setValue(voiceInformation);
        }
        
        if(voiceBack!=null) {
        	this.voiceBack = new ASNBoolean();
        	this.voiceBack.setValue(voiceBack);
        }
    }

    public Integer getMinimumNbOfDigits() {
    	if(minimumNbOfDigits==null || minimumNbOfDigits.getValue()==null)
    		return null;
    	
        return minimumNbOfDigits.getValue().intValue();
    }

    public int getMaximumNbOfDigits() {
    	if(maximumNbOfDigits==null || maximumNbOfDigits.getValue()==null)
    		return 0;
    	
        return maximumNbOfDigits.getValue().intValue();
    }

    public byte[] getEndOfReplyDigit() {
    	if(endOfReplyDigit==null)
    		return null;
    	
    	ByteBuf value=endOfReplyDigit.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public byte[] getCancelDigit() {
    	if(cancelDigit==null)
    		return null;
    	
    	ByteBuf value=cancelDigit.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public byte[] getStartDigit() {
    	if(startDigit==null)
    		return null;
    	
    	ByteBuf value=startDigit.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public Integer getFirstDigitTimeOut() {
    	if(firstDigitTimeOut==null || firstDigitTimeOut.getValue()==null)
    		return null;
    	
        return firstDigitTimeOut.getValue().intValue();
    }

    public Integer getInterDigitTimeOut() {
    	if(interDigitTimeOut==null || interDigitTimeOut.getValue()==null)
    		return null;
    	
        return interDigitTimeOut.getValue().intValue();
    }

    public ErrorTreatment getErrorTreatment() {
    	if(errorTreatment==null)
    		return null;
    	
        return errorTreatment.getType();
    }

    public Boolean getInterruptableAnnInd() {
    	if(interruptableAnnInd==null)
    		return null;
    	
        return interruptableAnnInd.getValue();
    }

    public Boolean getVoiceInformation() {
    	if(voiceInformation==null)
    		return null;
    	
        return voiceInformation.getValue();
    }

    public Boolean getVoiceBack() {
    	if(voiceBack==null)
    		return null;
    	
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
            sb.append(ASNOctetString.printDataArr(this.getEndOfReplyDigit()));
            sb.append("]");
        }
        if (this.cancelDigit != null) {
            sb.append(", cancelDigit=[");
            sb.append(ASNOctetString.printDataArr(this.getCancelDigit()));
            sb.append("]");
        }
        if (this.startDigit != null) {
            sb.append(", startDigit=[");
            sb.append(ASNOctetString.printDataArr(this.getStartDigit()));
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