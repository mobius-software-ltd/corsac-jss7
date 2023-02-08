/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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
		this.globalOperationCode=new ASNObjectIdentifier(globalOperationCode,"GlobalOperationCode",true,false);		
	}

	public Long getLocalOperationCode() {
		if(localOperationCode==null)
			return null;
		
		return localOperationCode.getValue();
	}

	public void setLocalOperationCode(Integer localOperationCode) {
		this.globalOperationCode=null;
		this.localOperationCode = new ASNInteger(localOperationCode,"LocalOperationCode",Integer.MIN_VALUE,Integer.MAX_VALUE,false);		
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