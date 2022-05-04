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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePart;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartDate;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartPrice;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartTime;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class VariablePartImpl implements VariablePart {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger integer;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup number;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = VariablePartTimeImpl.class)
    private VariablePartTime time;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = VariablePartDateImpl.class)
    private VariablePartDate date;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = VariablePartPriceImpl.class)
    private VariablePartPrice price;

    public VariablePartImpl() {
    }

    public VariablePartImpl(Integer integer) {
    	if(integer!=null)
    		this.integer = new ASNInteger(integer,"Integer",0,255,false);    		
    }

    public VariablePartImpl(DigitsIsup number) {
        this.number = number;
    }

    public VariablePartImpl(VariablePartTime time) {
        this.time = time;
    }

    public VariablePartImpl(VariablePartDate date) {
        this.date = date;
    }

    public VariablePartImpl(VariablePartPrice price) {
        this.price = price;
    }

    public Integer getInteger() {
    	if(integer==null)
    		return null;
    	
        return integer.getIntValue();
    }

    public DigitsIsup getNumber() {
    	if(number!=null)
    		number.setIsGenericDigits();
    	
        return number;
    }

    public VariablePartTime getTime() {
        return time;
    }

    public VariablePartDate getDate() {
        return date;
    }

    public VariablePartPrice getPrice() {
        return price;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("VariablePart [");

        if (this.integer != null) {
            sb.append("integer=");
            sb.append(integer.getValue());
        }
        if (this.number != null) {
            sb.append(" number=");
            sb.append(number.toString());
        }
        if (this.time != null) {
            sb.append(" time=");
            sb.append(time.toString());
        }
        if (this.date != null) {
            sb.append(" date=");
            sb.append(date.toString());
        }
        if (this.price != null) {
            sb.append(" price=");
            sb.append(price.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(integer==null && number==null && time==null && date==null && price==null)
			throw new ASNParsingComponentException("one of child items  should be set for variable part", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
