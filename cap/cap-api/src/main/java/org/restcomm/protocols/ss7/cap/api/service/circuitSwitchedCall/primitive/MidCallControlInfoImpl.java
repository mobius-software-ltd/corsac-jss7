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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.map.api.primitives.SingleTbcdString;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class MidCallControlInfoImpl {
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger minimumNumberOfDigits;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger maximumNumberOfDigits;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private SingleTbcdString endOfReplyDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private SingleTbcdString cancelDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private SingleTbcdString startDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private ASNInteger interDigitTimeout;

    public MidCallControlInfoImpl() {
    }

    public MidCallControlInfoImpl(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit, String cancelDigit, String startDigit,
            Integer interDigitTimeout) {
        if(minimumNumberOfDigits!=null) {
        	this.minimumNumberOfDigits = new ASNInteger();
        	this.minimumNumberOfDigits.setValue(minimumNumberOfDigits.longValue());        			
        }
        
        if(maximumNumberOfDigits!=null) {
        	this.maximumNumberOfDigits = new ASNInteger();
        	this.maximumNumberOfDigits.setValue(maximumNumberOfDigits.longValue());
        }
        
        if(endOfReplyDigit!=null)
        	this.endOfReplyDigit = new SingleTbcdString("endOfReplyDigit",endOfReplyDigit);
        
        if(cancelDigit!=null)
        	this.cancelDigit = new SingleTbcdString("cancelDigit",cancelDigit);
        
        if(startDigit!=null)
        	this.startDigit = new SingleTbcdString("startDigit",startDigit);
        
        if(interDigitTimeout!=null) {
            this.interDigitTimeout = new ASNInteger();
            this.interDigitTimeout.setValue(interDigitTimeout.longValue());
        }
    }

    public Integer getMinimumNumberOfDigits() {
    	if(this.minimumNumberOfDigits==null)
    		return null;
    	
        return minimumNumberOfDigits.getValue().intValue();
    }

    public Integer getMaximumNumberOfDigits() {
    	if(this.maximumNumberOfDigits==null)
    		return null;
    	
        return maximumNumberOfDigits.getValue().intValue();
    }

    public String getEndOfReplyDigit() {
    	if(endOfReplyDigit==null)
    		return null;
    	
        return endOfReplyDigit.getData();
    }

    public String getCancelDigit() {
    	if(cancelDigit==null)
    		return null;
    	
        return cancelDigit.getData();
    }

    public String getStartDigit() {
    	if(startDigit==null)
    		return null;
    	
        return startDigit.getData();
    }

    public Integer getInterDigitTimeout() {
    	if(interDigitTimeout==null)
    		return null;
    	
        return interDigitTimeout.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MidCallControlInfo [");

        if (this.minimumNumberOfDigits != null) {
            sb.append("minimumNumberOfDigits=");
            sb.append(minimumNumberOfDigits);
            sb.append(", ");
        }
        if (this.maximumNumberOfDigits != null) {
            sb.append("maximumNumberOfDigits=");
            sb.append(maximumNumberOfDigits);
            sb.append(", ");
        }
        if (this.endOfReplyDigit != null) {
            sb.append("endOfReplyDigit=\"");
            sb.append(endOfReplyDigit);
            sb.append("\", ");
        }
        if (this.cancelDigit != null) {
            sb.append("cancelDigit=\"");
            sb.append(cancelDigit);
            sb.append("\", ");
        }
        if (this.startDigit != null) {
            sb.append("startDigit=\"");
            sb.append(startDigit);
            sb.append("\", ");
        }
        if (this.interDigitTimeout != null) {
            sb.append("interDigitTimeout=");
            sb.append(interDigitTimeout);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
