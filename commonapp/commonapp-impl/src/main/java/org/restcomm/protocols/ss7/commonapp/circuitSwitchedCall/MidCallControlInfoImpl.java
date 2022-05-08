/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MidCallControlInfo;
import org.restcomm.protocols.ss7.commonapp.primitives.SingleTbcdStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class MidCallControlInfoImpl implements MidCallControlInfo {
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger minimumNumberOfDigits;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger maximumNumberOfDigits;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private SingleTbcdStringImpl endOfReplyDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private SingleTbcdStringImpl cancelDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private SingleTbcdStringImpl startDigit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private ASNInteger interDigitTimeout;

    public MidCallControlInfoImpl() {
    }

    public MidCallControlInfoImpl(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit, String cancelDigit, String startDigit,
            Integer interDigitTimeout) {
        if(minimumNumberOfDigits!=null)
        	this.minimumNumberOfDigits = new ASNInteger(minimumNumberOfDigits,"MinimumNumberOfDigits",1,30,false);
        	
        if(maximumNumberOfDigits!=null)
        	this.maximumNumberOfDigits = new ASNInteger(maximumNumberOfDigits,"MaximumNumberOfDigits",1,30,false);
        	
        if(endOfReplyDigit!=null)
        	this.endOfReplyDigit = new SingleTbcdStringImpl("endOfReplyDigit",endOfReplyDigit);
        
        if(cancelDigit!=null)
        	this.cancelDigit = new SingleTbcdStringImpl("cancelDigit",cancelDigit);
        
        if(startDigit!=null)
        	this.startDigit = new SingleTbcdStringImpl("startDigit",startDigit);
        
        if(interDigitTimeout!=null)
            this.interDigitTimeout = new ASNInteger(interDigitTimeout,"InterDigitTimeout",1,127,false);            
    }

    public Integer getMinimumNumberOfDigits() {
    	if(this.minimumNumberOfDigits==null)
    		return 1;
    	
        return minimumNumberOfDigits.getIntValue();
    }

    public Integer getMaximumNumberOfDigits() {
    	if(this.maximumNumberOfDigits==null)
    		return 30;
    	
        return maximumNumberOfDigits.getIntValue();
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
    		return 10;
    	
        return interDigitTimeout.getIntValue();
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