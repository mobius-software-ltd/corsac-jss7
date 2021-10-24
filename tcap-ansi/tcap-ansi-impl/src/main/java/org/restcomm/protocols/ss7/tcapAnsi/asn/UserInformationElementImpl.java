package org.restcomm.protocols.ss7.tcapAnsi.asn;

import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;

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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

import io.netty.buffer.ByteBuf;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0x08,constructed=true,lengthIndefinite=false)
public class UserInformationElementImpl implements UserInformationElement {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x08,constructed=true,index=-1)
	private UserInformationExternalImpl ext;

	public void setIdentifier(Long value) {
		if(value==null)
			return;
		
		if(ext==null)
			ext=new UserInformationExternalImpl();
		
		ext.setIdentifier(value);
	}
	
	public void setIdentifier(String value) {
		if(value==null)
			return;
		
		if(ext==null)
			ext=new UserInformationExternalImpl();
		
		ext.setIdentifier(value);
	}
	
	public void setIdentifier(List<Long> ids) {
		if(ids==null)
			return;
		
		if(ext==null)
			ext=new UserInformationExternalImpl();
		
		ext.setIdentifier(ids);
	}
	
	public void setChild(ByteBuf value) {
		if(value==null)
			return;
		
		if(ext==null)
			ext=new UserInformationExternalImpl();
		
		ext.setChild(value);
	}
	
	public void setChild(ASNBitString bitString) {
		if(bitString==null)
			return;
		
		if(ext==null)
			ext=new UserInformationExternalImpl();
		
		ext.setChild(bitString);
	}
	
	public void setChildAsObject(Object value) {
		if(value==null)
			return;
		
		if(ext==null)
			ext=new UserInformationExternalImpl();
		
		ASNUserInformationObjectImpl realValue=new ASNUserInformationObjectImpl();
		realValue.setValue(value);
		ext.setChildAsObject(realValue);		
	}
	
	public Boolean isIDIndirect() {
		if(ext==null)
			return false;
		
		return ext.isIDIndirect();
	}
	
	public Boolean isIDObjectIdentifier() {
		if(ext==null)
			return false;
		
		return ext.isIDObjectIdentifier();
	}	
	
	public Boolean isIDDescriptor() {
		if(ext==null)
			return false;
		
		return ext.isIDDescriptor();
	}
	
	public Boolean isValueObject() {
		if(ext==null)
			return false;
		
		return ext.isValueObject();
	}
	
	public Boolean isValueString() {
		if(ext==null)
			return false;
		
		return ext.isValueString();
	}	
	
	public Boolean isValueBitString() {
		if(ext==null)
			return false;
		
		return ext.isValueBitString();
	}

	public Long getIndirectReference() {
		if(ext==null)
			return null;
		
		return ext.getIndirectReference();
	}

	public List<Long> getObjectIdentifier() {
		if(ext==null)
			return null;
		
		return ext.getObjectIdentifier();
	}

	public String getDescriptor() {
		if(ext==null)
			return null;
		
		return ext.getDescriptor();
	}

	public Object getChild() {
		if(ext==null || ext.getChild()==null)
			return null;
		
		return ext.getChild().getValue();
	}

	public ByteBuf getChildString() {
		if(ext==null)
			return null;
		
		return ext.getChildString();
	}

	public ASNBitString getBitString() {
		if(ext==null)
			return null;
		
		return ext.getBitString();
	}
}