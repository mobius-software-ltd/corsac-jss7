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

package org.restcomm.protocols.ss7.inap.errors;

import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageOctetString;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
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
	protected Long errorCode;

	private ASNOctetString value;
	
    protected INAPErrorMessageOctetStringImpl(Long errorCode) {
        this.errorCode = errorCode;
    }

    public INAPErrorMessageOctetStringImpl() {
    }

    protected void setValue(ByteBuf value) {
    	if(value!=null)
    		this.value=new ASNOctetString(value);
    	else
    		this.value=null;
	}
	
    public Long getErrorCode() {
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
}
