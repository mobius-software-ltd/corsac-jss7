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

package org.restcomm.protocols.ss7.cap.errors;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

/**
 * Base class of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNWrappedTag
public abstract class EnumeratedСAPErrorMessage1Impl extends CAPErrorMessageImpl {
	protected Integer errorCode;

	private ASNEnumerated value;
	private String name;
	private Integer minValue;
	private Integer maxValue;
	
    protected EnumeratedСAPErrorMessage1Impl(Integer errorCode,String name,Integer minValue,Integer maxValue) {
        this.errorCode = errorCode;
        this.name=name;
        this.minValue=minValue;
        this.maxValue=maxValue;
    }

    public EnumeratedСAPErrorMessage1Impl(String name,Integer minValue,Integer maxValue) {
    	this.name=name;
    	this.minValue=minValue;
    	this.maxValue=maxValue;
    }

    protected Integer getValue() {
    	if(this.value==null)
    		return null;
    	
		return value.getIntValue();
	}

    protected void setValue(Integer value) {
    	if(this.value==null)
    		this.value=new ASNEnumerated(value,name,minValue,maxValue,true);    	
	}
	
    public Integer getErrorCode() {
        return errorCode;
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(value==null)
			throw new ASNParsingComponentException("value not set for " + name, ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
