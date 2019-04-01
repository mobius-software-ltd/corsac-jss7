package com.mobius.software.telco.protocols.ss7.asn;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

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

public class ASNHeader {
	
	private ASNClass asnClass;
	private Boolean isConstructed;
	private Integer asnTag;
	private Boolean indefiniteLength;
	private Integer index;
	
	public ASNHeader(ASNClass asnClass,Boolean isConstructed,Integer asnTag,Boolean indefiniteLength,Integer index) {
		this.asnClass=asnClass;
		this.isConstructed=isConstructed;
		this.asnTag=asnTag;
		this.indefiniteLength=indefiniteLength;
		this.index=index;
	}
	
	public ASNHeader(ASNTag asnTag,ASNClass realClass, Integer realTag,Boolean realConstructed,Integer index) {
		this.asnClass=realClass;
		this.isConstructed=realConstructed;
		this.asnTag=realTag;
		this.indefiniteLength=asnTag.lengthIndefinite();
		this.index=index;
	}

	public ASNClass getAsnClass() {
		return asnClass;
	}

	public Boolean getIsConstructed() {
		return isConstructed;
	}

	public Integer getAsnTag() {
		return asnTag;
	}

	public Boolean getIndefiniteLength() {
		return indefiniteLength;
	}

	public Integer getIndex() {
		return index;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asnClass == null) ? 0 : asnClass.hashCode());
		result = prime * result + ((asnTag == null) ? 0 : asnTag.hashCode());
		result = prime * result + ((isConstructed == null) ? 0 : isConstructed.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
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
		
		ASNHeader other = (ASNHeader) obj;
		if (asnClass != other.asnClass)
			return false;
		
		if (asnTag == null) {
			if (other.asnTag != null)
				return false;
		} else if (!asnTag.equals(other.asnTag))
			return false;
		
		if (isConstructed == null) {
			if (other.isConstructed != null)
				return false;
		} else if (!isConstructed.equals(other.isConstructed))
			return false;
		
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false; 
		
		return true;
	}			
}