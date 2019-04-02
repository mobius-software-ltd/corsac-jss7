package org.restcomm.protocols.ss7.tcap.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

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

/**
*
* @author yulian oifa
*
*/

public class LocalErrorCodeImpl extends ASNInteger implements ErrorCode {
	public void setLocalErrorCode(Long localErrorCode) {
        this.setValue(localErrorCode);
    }

    public Long getLocalErrorCode() {
        return this.getValue();
    }
    
    public ErrorCodeType getErrorType() {
        return ErrorCodeType.Local;
    }

    public String getStringValue() {
        if (this.getValue() != null)
            return this.getValue().toString();
        else
            return "empty";
    }

    public String toString() {
        if (this.getValue() != null)
            return "ErrorCode[errorCodeType=Local, data=" + this.getValue().toString() + "]";
        else
            return "ErrorCode[empty]";
    }
}