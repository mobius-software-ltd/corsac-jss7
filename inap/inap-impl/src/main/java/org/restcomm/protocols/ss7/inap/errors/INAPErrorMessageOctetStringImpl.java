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

package org.restcomm.protocols.ss7.inap.errors;

import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageOctetString;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * Base class of INAP CS1+ messages
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class INAPErrorMessageOctetStringImpl extends INAPErrorMessageImpl implements INAPErrorMessageOctetString {
	protected Integer errorCode;

	private ASNOctetString value;
	
    protected INAPErrorMessageOctetStringImpl(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public INAPErrorMessageOctetStringImpl() {
    }

    protected void setValue(ByteBuf value) {
    	if(value!=null)
    		this.value=new ASNOctetString(value,"Error",null,null,false);
    	else
    		this.value=null;
	}
	
    public Integer getErrorCode() {
        return errorCode;
    }

    public ByteBuf getValue() {
    	if(value==null)
    		return null;
    	
    	return value.getValue();
    }
    
    @Override
    public String toString() {
    	if(getValue()!=null)
    		return "INAPErrorMessageOctetString [errorCode=" + errorCode + ":" + inapErrorCodeName() + ",data=" + value.printDataArr() + "]";
    	else
    		return "INAPErrorMessageOctetString [errorCode=" + errorCode + ":" + inapErrorCodeName() + "]";
    }

    private String inapErrorCodeName() {
        if (errorCode == null)
            return "N/A";
        switch (errorCode.intValue()) {
            case INAPErrorCode.congestion:
                return "congestion";
            case INAPErrorCode.errorInParameterValue:
                return "errorInParameterValue";
            case INAPErrorCode.executionError:
                return "executionError";
            case INAPErrorCode.illegalCombinationOfParameters:
                return "illegalCombinationOfParameters";
            case INAPErrorCode.infoNotAvailable:
                return "infoNotAvailable";
            case INAPErrorCode.invalidDataItemID:
                return "invalidDataItemID";
            case INAPErrorCode.notAuthorized:
                return "notAuthorized";
            case INAPErrorCode.parameterMissing:
                return "parameterMissing";
            case INAPErrorCode.otherError:
                return "otherError";
            default:
                return errorCode.toString();
        }
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(value==null)
			throw new ASNParsingComponentException("value not set for " + inapErrorCodeName(), ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
