/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContextType;

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
public class ApplicationContextImpl implements ApplicationContext {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=27,constructed=false,index=-1)
	private ASNInteger intApplicationContext;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=28,constructed=false,index=-1)
	private ASNObjectIdentifier objApplicationContext;
    
    public ApplicationContextImpl() {
        super();
    }
    
    public ApplicationContextType getType() {
    	if(intApplicationContext!=null)
    		return ApplicationContextType.Integer;
    	
    	return ApplicationContextType.ObjectId;
    }
    
    public List<Long> getObj() {
		if(objApplicationContext==null)
			return null;
		
		return objApplicationContext.getValue();
	}

	public void setObj(List<Long> value) {
		this.intApplicationContext=null;
		this.objApplicationContext=new ASNObjectIdentifier(value,"AppCtx",true,false);		
	}

	public Integer getInt() {
		if(intApplicationContext==null)
			return null;
		
		return intApplicationContext.getIntValue();
	}

	public void setInt(int value) {
		this.objApplicationContext=null;
		this.intApplicationContext = new ASNInteger(value,"AppCtx",Integer.MIN_VALUE,Integer.MAX_VALUE,false);		
	}
}