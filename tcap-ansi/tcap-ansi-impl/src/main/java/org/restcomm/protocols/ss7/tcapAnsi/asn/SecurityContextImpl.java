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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContextType;

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
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=true,lengthIndefinite=false)
public class SecurityContextImpl implements SecurityContext {
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
	private ASNInteger intSecurityContext;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
	private ASNObjectIdentifier objSecurityContext;
    
    public SecurityContextImpl() {
        super();
    }
    
    public SecurityContextType getType() {
    	if(intSecurityContext!=null)
    		return SecurityContextType.Integer;
    	
    	return SecurityContextType.ObjectId;
    }
    
    public List<Long> getObj() {
		if(objSecurityContext==null)
			return null;
		
		return objSecurityContext.getValue();
	}

	public void setObj(List<Long> value) {
		this.intSecurityContext=null;
		this.objSecurityContext=new ASNObjectIdentifier(value,"SecurityCtx",true,false);		
	}

	public Integer getInt() {
		if(intSecurityContext==null)
			return null;
		
		return intSecurityContext.getIntValue();
	}

	public void setInt(Integer value) {
		this.objSecurityContext=null;
		this.intSecurityContext = new ASNInteger(value,"SecurityCtx",Integer.MIN_VALUE,Integer.MAX_VALUE,false);		
	}
}