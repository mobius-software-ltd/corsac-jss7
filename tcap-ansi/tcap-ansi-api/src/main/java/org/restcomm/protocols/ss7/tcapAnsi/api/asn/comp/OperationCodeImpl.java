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
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=true,lengthIndefinite=false)
public class OperationCodeImpl {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=16,constructed=false,index=-1)
	private ASNInteger nationalOperationCode;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=17,constructed=false,index=-1)
	private ASNInteger privateOperationCode;
    
    public Long getNationalOperationCode() {
		if(nationalOperationCode==null)
			return null;
		
		return nationalOperationCode.getValue();
	}

	public void setNationalOperationCode(Long nationalOperationCode) {
		this.privateOperationCode=null;
		this.nationalOperationCode=new ASNInteger();
		this.nationalOperationCode.setValue(nationalOperationCode);		
	}

	public Long getPrivateOperationCode() {
		if(privateOperationCode==null)
			return null;
		
		return privateOperationCode.getValue();
	}

	public void setPrivateOperationCode(Long privateOperationCode) {
		this.nationalOperationCode=null;
		this.privateOperationCode = new ASNInteger();
		this.privateOperationCode.setValue(privateOperationCode);
	}

	public OperationCodeType getOperationType() {
		if(this.nationalOperationCode!=null)
			return OperationCodeType.National;
		else
			return OperationCodeType.Private;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getOperationType().hashCode();
		result = prime * result + ((nationalOperationCode == null) ? 0 : nationalOperationCode.hashCode());
		result = prime * result + ((privateOperationCode == null) ? 0 : privateOperationCode.hashCode());
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
		
		OperationCodeImpl other = (OperationCodeImpl) obj;
		if(getOperationType()!=other.getOperationType())
			return false;
		
		if (nationalOperationCode == null) {
			if (other.nationalOperationCode != null)
				return false;
		} else if (!nationalOperationCode.equals(other.nationalOperationCode))
			return false;
		
		if (privateOperationCode == null) {
			if (other.privateOperationCode != null)
				return false;
		} else if (!privateOperationCode.equals(other.privateOperationCode))
			return false;
		
		return true;
	}	
}
