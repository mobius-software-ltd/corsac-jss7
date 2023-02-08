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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCodeType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=true,lengthIndefinite=false)
public class OperationCodeImpl implements OperationCode {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=16,constructed=false,index=-1)
	private ASNInteger nationalOperationCode;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=17,constructed=false,index=-1)
	private ASNInteger privateOperationCode;
    
    public Integer getNationalOperationCode() {
		if(nationalOperationCode==null)
			return null;
		
		return nationalOperationCode.getIntValue();
	}

	public void setNationalOperationCode(Integer nationalOperationCode) {
		this.privateOperationCode=null;
		this.nationalOperationCode=new ASNInteger(nationalOperationCode,"OperationCode",-32768,32767,false);		
	}

	public Integer getPrivateOperationCode() {
		if(privateOperationCode==null)
			return null;
		
		return privateOperationCode.getIntValue();
	}

	public void setPrivateOperationCode(Integer privateOperationCode) {
		this.nationalOperationCode=null;
		this.privateOperationCode = new ASNInteger(privateOperationCode,"OperationCode",Integer.MIN_VALUE,Integer.MAX_VALUE,false);		
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
