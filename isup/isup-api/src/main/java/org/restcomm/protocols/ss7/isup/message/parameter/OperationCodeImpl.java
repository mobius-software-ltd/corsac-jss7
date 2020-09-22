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

package org.restcomm.protocols.ss7.isup.message.parameter;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0,constructed=true,lengthIndefinite=false)
public class OperationCodeImpl {
	// mandatory
	private ASNObjectIdentifier globalOperationCode;
	private ASNInteger localOperationCode;
	    
	public List<Long> getGlobalOperationCode() {
		if(globalOperationCode==null)
			return null;
		
		return globalOperationCode.getValue();
	}

	public void setGlobalOperationCode(List<Long> globalOperationCode) {
		this.localOperationCode=null;
		this.globalOperationCode=new ASNObjectIdentifier();
		this.globalOperationCode.setValue(globalOperationCode);		
	}

	public Long getLocalOperationCode() {
		if(localOperationCode==null)
			return null;
		
		return localOperationCode.getValue();
	}

	public void setLocalOperationCode(Long localOperationCode) {
		this.globalOperationCode=null;
		this.localOperationCode = new ASNInteger();
		this.localOperationCode.setValue(localOperationCode);
	}

	public OperationCodeType getOperationType() {
		if(this.localOperationCode!=null)
			return OperationCodeType.Local;
		else
			return OperationCodeType.Global;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getOperationType().hashCode();
		result = prime * result + ((globalOperationCode == null) ? 0 : globalOperationCode.hashCode());
		result = prime * result + ((localOperationCode == null) ? 0 : localOperationCode.hashCode());
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
		
		if (globalOperationCode == null) {
			if (other.globalOperationCode != null)
				return false;
		} else if (!globalOperationCode.equals(other.globalOperationCode))
			return false;
		if (localOperationCode == null) {
			if (other.localOperationCode != null)
				return false;
		} else if (!localOperationCode.equals(other.localOperationCode))
			return false;
		
		return true;
	}
}