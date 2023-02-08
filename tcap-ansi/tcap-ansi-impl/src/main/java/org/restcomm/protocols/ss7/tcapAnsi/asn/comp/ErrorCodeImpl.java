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

package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCodeType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=true,lengthIndefinite=false)
public class ErrorCodeImpl implements ErrorCode {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=20,constructed=false,index=-1)
	private ASNInteger privateErrorCode;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=19,constructed=false,index=-1)
	private ASNInteger nationalErrorCode;
    
    public Integer getNationalErrorCode() {
		if(nationalErrorCode==null)
			return null;
		
		return nationalErrorCode.getIntValue();
	}

	public void setNationalErrorCode(Integer nationalErrorCode) {
		this.privateErrorCode=null;
		this.nationalErrorCode=new ASNInteger(nationalErrorCode,"ErrorCode",-128,127,false);		
	}

	public Integer getPrivateErrorCode() {
		if(privateErrorCode==null)
			return null;
		
		return privateErrorCode.getIntValue();
	}

	public void setPrivateErrorCode(Integer privateErrorCode) {
		this.nationalErrorCode=null;
		this.privateErrorCode = new ASNInteger(privateErrorCode,"ErrorCode",Integer.MIN_VALUE,Integer.MAX_VALUE,false);		
	}

	public ErrorCodeType getErrorType() {
		if(this.nationalErrorCode!=null)
			return ErrorCodeType.National;
		else
			return ErrorCodeType.Private;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getErrorType().hashCode();
		result = prime * result + ((nationalErrorCode == null) ? 0 : nationalErrorCode.hashCode());
		result = prime * result + ((privateErrorCode == null) ? 0 : privateErrorCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		ErrorCodeImpl other = (ErrorCodeImpl) obj;
		if(getErrorType()!=other.getErrorType())
			return false;
		
		if (nationalErrorCode == null) {
			if (other.nationalErrorCode != null)
				return false;
		} else if (!nationalErrorCode.equals(other.nationalErrorCode))
			return false;
		
		if (privateErrorCode == null) {
			if (other.privateErrorCode != null)
				return false;
		} else if (!privateErrorCode.equals(other.privateErrorCode))
			return false;
		
		return true;
	}	
}
