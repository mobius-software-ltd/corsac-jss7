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

package org.restcomm.protocols.ss7.tcap.asn.tx;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

/**
 * @author baranowb
 * @author yulianoifa
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
