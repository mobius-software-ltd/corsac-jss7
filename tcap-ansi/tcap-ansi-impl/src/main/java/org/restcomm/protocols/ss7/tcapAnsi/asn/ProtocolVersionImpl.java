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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=26,constructed=false,lengthIndefinite=false)
public class ProtocolVersionImpl extends ASNSingleByte implements ProtocolVersion {
	private static int _TAG_T1_114_1996 = 0x01;
	private static int _TAG_T1_114_2000 = 0x02;

	/**
     * Creating ProtocolVersion that support both T1_114_1996Supported and T1_114_2000Supported
     */
    public ProtocolVersionImpl() {
    	super(_TAG_T1_114_1996 + _TAG_T1_114_2000,"ProtocolVersion",0,3,false);        
    }

    public ProtocolVersionImpl(boolean isT1_114_1996Supported,boolean isT1_114_2000Supported) {
    	super(translate(isT1_114_1996Supported, isT1_114_2000Supported),"ProtocolVersion",0,3,false);    	
    }
    
    private static Integer translate(boolean isT1_114_1996Supported,boolean isT1_114_2000Supported) {
    	if(isT1_114_1996Supported) {
	    	if(isT1_114_2000Supported)
	    		return _TAG_T1_114_1996 + _TAG_T1_114_2000;
	    	else
	    		return _TAG_T1_114_1996;
    	} else {
    		if(isT1_114_2000Supported)
    			return _TAG_T1_114_2000;
	    	else
	    		return 0;
    	}
    }
    
    public boolean isT1_114_1996Supported() {
    	Integer value=getValue();
    	if(value==null || (value & _TAG_T1_114_1996)==0)
    		return false;
    	
    	return true;
    }

    public boolean isT1_114_2000Supported() {
    	Integer value=getValue();
    	if(value==null || (value & _TAG_T1_114_2000)==0)
    		return false;
    	
    	return true;
    }

    public boolean isSupportedVersion() {
    	Integer value=getValue();
    	return value!=null && value!=0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProtocolVersion[");
        if (this.isT1_114_1996Supported()) {
            sb.append("T1_114_1996Supported, ");
        }
        if (this.isT1_114_2000Supported()) {
            sb.append("T1_114_2000Supported, ");
        }
        sb.append("]");
        return sb.toString();
    }
}
