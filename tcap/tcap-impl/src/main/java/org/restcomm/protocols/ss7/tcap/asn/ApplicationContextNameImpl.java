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

package org.restcomm.protocols.ss7.tcap.asn;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

/**
 * @author baranowb
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=true,lengthIndefinite=false)
public class ApplicationContextNameImpl implements ApplicationContextName {
	ASNObjectIdentifier realValue;
	
    public String getStringValue() {
    	if(realValue==null || realValue.getValue()==null)
    		return null;
    	
        return realValue.getValue().toString();
    }

    public List<Long> getOid() {
    	if(realValue==null)
    		return null;
    	
    	return realValue.getValue();
    }
    
    public void setOid(List<Long> value) {
    	realValue=new ASNObjectIdentifier(value,"AppNameCtx",true,false);
    }
    
    public String toString() {
    	List<Long> values=null;
    	if(realValue!=null)
    		values=realValue.getValue();
    	
        return "ApplicationContextName[oid=" + values + "]";
    }

}
