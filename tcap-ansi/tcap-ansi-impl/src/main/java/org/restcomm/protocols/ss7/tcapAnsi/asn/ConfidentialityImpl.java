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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.Confidentiality;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,lengthIndefinite=false)
public class ConfidentialityImpl implements Confidentiality {
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=-1)
	private ASNInteger integerConfidentialityId;
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=false,index=-1)
	private ASNObjectIdentifier objectConfidentialityId;

    public Integer getIntegerConfidentialityId() {
    	if(integerConfidentialityId==null)
    		return null;
    	
        return integerConfidentialityId.getIntValue();
    }

    public void setIntegerConfidentialityId(Integer val) {
        integerConfidentialityId = new ASNInteger(val,"ConfidentialityID",Integer.MIN_VALUE,Integer.MAX_VALUE,false);
        objectConfidentialityId = null;
    }

    public List<Long> getObjectConfidentialityId() {
    	if(objectConfidentialityId==null)
    		return null;
    	
        return objectConfidentialityId.getValue();
    }

    public void setObjectConfidentialityId(List<Long> val) {
        integerConfidentialityId = null;
        objectConfidentialityId = new ASNObjectIdentifier(val,"ConfidentialityID",true,false);        
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Confidentiality[");
        if (this.integerConfidentialityId != null) {
            sb.append("integerConfidentialityId=");
            sb.append(this.integerConfidentialityId.getValue());
        } else if (this.objectConfidentialityId != null) {
            sb.append("objectConfidentialityId=");
            sb.append(objectConfidentialityId.getValue());
        }
        sb.append("]");
        return sb.toString();
    }

}
