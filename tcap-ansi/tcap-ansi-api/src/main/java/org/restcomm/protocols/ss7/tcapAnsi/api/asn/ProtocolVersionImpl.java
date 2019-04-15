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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=26,constructed=false,lengthIndefinite=false)
public class ProtocolVersionImpl extends ASNOctetString {
	byte _TAG_T1_114_1996 = 0x01;
	byte _TAG_T1_114_2000 = 0x02;

    /**
     * Creating ProtocolVersion that support both T1_114_1996Supported and T1_114_2000Supported
     */
    public ProtocolVersionImpl() {
    	setValue(new byte[] { (byte)(_TAG_T1_114_1996 + _TAG_T1_114_2000) });        
    }

    public boolean isT1_114_1996Supported() {
    	byte[] data=this.getValue();
        if ((data[0] & _TAG_T1_114_1996) != 0)
            return true;
        else
            return false;
    }

    public boolean isT1_114_2000Supported() {
    	byte[] data=this.getValue();
        if ((data[0] & _TAG_T1_114_2000) != 0)
            return true;
        else
            return false;
    }

    public boolean isSupportedVersion() {
    	byte[] data=this.getValue();
        if ((data!=null && data.length>0 && (data[0] & _TAG_T1_114_1996) != 0 || (data[0] & _TAG_T1_114_1996) != 0))
            return true;
        else
            return false;
    }

    public void setT1_114_1996Supported(boolean val) {
    	byte oldValue=0;
    	byte[] data=this.getValue();
    	if(data!=null && data.length>0)
    		oldValue=data[0];
    	
        if (val)
            setValue(new byte[] {(byte)(oldValue | _TAG_T1_114_1996)});
        else
        	setValue(new byte[] {(byte)(oldValue & (_TAG_T1_114_1996 ^ 0xFF))});            
    }

    public void setT1_114_2000Supported(boolean val) {
    	byte oldValue=0;
    	byte[] data=this.getValue();
    	if(data!=null && data.length>0)
    		oldValue=data[0];
    	
        if (val)
            setValue(new byte[] {(byte)(oldValue | _TAG_T1_114_2000)});
        else
        	setValue(new byte[] {(byte)(oldValue & (_TAG_T1_114_2000 ^ 0xFF))});         
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
