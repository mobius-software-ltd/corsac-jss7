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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

/**
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=true,lengthIndefinite=false)
public class ApplicationContextNameImpl {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=27,constructed=false,index=-1)
	private ASNInteger intApplicationContext;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=28,constructed=false,index=-1)
	private ASNObjectIdentifier objApplicationContext;
    
    public ApplicationContextNameImpl() {
        super();
    }
    
    public ApplicationContextNameType getType() {
    	if(intApplicationContext!=null)
    		return ApplicationContextNameType.Integer;
    	
    	return ApplicationContextNameType.ObjectId;
    }
    
    public List<Long> getObj() {
		if(objApplicationContext==null)
			return null;
		
		return objApplicationContext.getValue();
	}

	public void setObj(List<Long> value) {
		this.intApplicationContext=null;
		this.objApplicationContext=new ASNObjectIdentifier();
		this.objApplicationContext.setValue(value);		
	}

	public Long getInt() {
		if(intApplicationContext==null)
			return null;
		
		return intApplicationContext.getValue();
	}

	public void setInt(Long value) {
		this.objApplicationContext=null;
		this.intApplicationContext = new ASNInteger();
		this.intApplicationContext.setValue(value);
	}
}