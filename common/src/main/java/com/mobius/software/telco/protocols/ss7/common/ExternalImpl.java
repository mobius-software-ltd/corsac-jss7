package com.mobius.software.telco.protocols.ss7.common;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNGeneric;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNGraphicString;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

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

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0x08,constructed=true,lengthIndefinite=false)
public abstract class ExternalImpl<T extends ASNGeneric>
{
	private ASNInteger indirectReference;
	private ASNObjectIdentifier objectIdentifier;
	private ASNGraphicString descriptor;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=true,index=1)
	private T child;
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=false,index=1)	
	private ASNOctetString childString;
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x02,constructed=false,index=1)
	private ASNBitString bitString;
	
	public void setIdentifier(Long value) {
		this.objectIdentifier=null;
		this.descriptor=null;
		this.indirectReference=new ASNInteger(value);		
	}
	
	public void setIdentifier(String value) {
		this.objectIdentifier=null;
		this.indirectReference=null;
		this.descriptor=new ASNGraphicString(value);		
	}
	
	public void setIdentifier(List<Long> ids) {
		this.descriptor=null;
		this.indirectReference=null;
		this.objectIdentifier=new ASNObjectIdentifier(ids);		
	}
	
	public void setChild(ByteBuf value) {
		this.childString=new ASNOctetString(value);
		this.child=null;
		this.bitString=null;
	}
	
	public void setChild(ASNBitString bitString) {
		this.childString=null;
		this.child=null;
		this.bitString=bitString;
	}
	
	public void setChildAsObject(T value) {
		this.child=value;
		this.bitString=null;
		this.childString=null;
	}
	
	public Boolean isIDIndirect() {
		return indirectReference!=null;
	}
	
	public Boolean isIDObjectIdentifier() {
		return objectIdentifier!=null;
	}	
	
	public Boolean isIDDescriptor() {
		return this.descriptor!=null;
	}
	
	public Boolean isValueObject() {
		return child!=null;
	}
	
	public Boolean isValueString() {
		return childString!=null;
	}	
	
	public Boolean isValueBitString() {
		return this.bitString!=null;
	}

	public Long getIndirectReference() {
		if(indirectReference==null)
			return null;
		
		return indirectReference.getValue();
	}

	public List<Long> getObjectIdentifier() {
		if(objectIdentifier==null)
			return null;
	
		return objectIdentifier.getValue();
	}

	public String getDescriptor() {
		if(descriptor==null)
			return null;
		
		return descriptor.getValue();
	}

	public T getChild() {
		return child;
	}

	public ByteBuf getChildString() {
		if(childString==null)
			return null;
		
		return childString.getValue();
	}

	public ASNBitString getBitString() {
		return bitString;
	}		
}