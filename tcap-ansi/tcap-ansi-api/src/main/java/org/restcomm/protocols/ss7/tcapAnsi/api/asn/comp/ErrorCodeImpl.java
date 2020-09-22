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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=true,lengthIndefinite=false)
public class ErrorCodeImpl {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=20,constructed=false,index=-1)
	private ASNInteger privateErrorCode;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=19,constructed=false,index=-1)
	private ASNInteger nationalErrorCode;
    
    public Long getNationalErrorCode() {
		if(nationalErrorCode==null)
			return null;
		
		return nationalErrorCode.getValue();
	}

	public void setNationalErrorCode(Long nationalErrorCode) {
		this.privateErrorCode=null;
		this.nationalErrorCode=new ASNInteger();
		this.nationalErrorCode.setValue(nationalErrorCode);		
	}

	public Long getPrivateErrorCode() {
		if(privateErrorCode==null)
			return null;
		
		return privateErrorCode.getValue();
	}

	public void setPrivateErrorCode(Long privateErrorCode) {
		this.nationalErrorCode=null;
		this.privateErrorCode = new ASNInteger();
		this.privateErrorCode.setValue(privateErrorCode);
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
