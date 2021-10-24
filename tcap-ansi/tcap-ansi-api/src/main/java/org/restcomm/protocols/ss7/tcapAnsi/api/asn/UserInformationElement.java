/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

import io.netty.buffer.ByteBuf;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0x08,constructed=true,lengthIndefinite=false)
public interface UserInformationElement {

	public void setIdentifier(Long value);
	
	public void setIdentifier(String value) ;
	
	public void setIdentifier(List<Long> ids);
	
	public void setChild(ByteBuf value);
	
	public void setChild(ASNBitString bitString);
	
	public void setChildAsObject(Object value);
	
	public Boolean isIDIndirect();
	
	public Boolean isIDObjectIdentifier();
	
	public Boolean isIDDescriptor();
	
	public Boolean isValueObject();
	
	public Boolean isValueString();
	
	public Boolean isValueBitString();

	public Long getIndirectReference();

	public List<Long> getObjectIdentifier();

	public String getDescriptor();

	public Object getChild();

	public ByteBuf getChildString();

	public ASNBitString getBitString();
}