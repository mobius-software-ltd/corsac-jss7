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

package org.restcomm.protocols.ss7.isup.message.parameter;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=true,lengthIndefinite=false)
public class ErrorCodeImpl {
	// mandatory
	private ASNObjectIdentifier globalErrorCode;
	private ASNInteger localErrorCode;
		    
	public List<Long> getGlobalErrorCode() {
		if(globalErrorCode==null)
			return null;
		
		return globalErrorCode.getValue();
	}

	public void setGlobalErrorCode(List<Long> globalErrorCode) {
		this.localErrorCode=null;
		this.globalErrorCode=new ASNObjectIdentifier(globalErrorCode,"GlobalErrorCode",true,false);			
	}

	public Long getLocalErrorCode() {
		if(localErrorCode==null)
			return null;
		
		return localErrorCode.getValue();
	}

	public void setLocalErrorCode(Integer localErrorCode) {
		this.globalErrorCode=null;
		this.localErrorCode = new ASNInteger(localErrorCode,"LocalErrorCode",Integer.MIN_VALUE,Integer.MAX_VALUE,false);		
	}

	public ErrorCodeType getErrorType() {
		if(this.localErrorCode!=null)
			return ErrorCodeType.Local;
		else
			return ErrorCodeType.Global;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getErrorType().hashCode();
		result = prime * result + ((globalErrorCode == null) ? 0 : globalErrorCode.hashCode());
		result = prime * result + ((localErrorCode == null) ? 0 : localErrorCode.hashCode());
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
		
		if (globalErrorCode == null) {
			if (other.globalErrorCode != null)
				return false;
		} else if (!globalErrorCode.equals(other.globalErrorCode))
			return false;
		
		if (localErrorCode == null) {
			if (other.localErrorCode != null)
				return false;
		} else if (!localErrorCode.equals(other.localErrorCode))
			return false;
		
		return true;
	}
}