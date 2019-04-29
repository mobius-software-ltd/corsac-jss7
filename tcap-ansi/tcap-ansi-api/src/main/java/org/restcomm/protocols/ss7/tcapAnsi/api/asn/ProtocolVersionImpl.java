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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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
	private static byte _TAG_T1_114_1996 = 0x01;
	private static byte _TAG_T1_114_2000 = 0x02;

	private static final ByteBuf T1_EMPTY=Unpooled.wrappedBuffer(new byte[] { 0x00 });
	private static final ByteBuf T1_1996=Unpooled.wrappedBuffer(new byte[] {_TAG_T1_114_1996});
	private static final ByteBuf T1_2000=Unpooled.wrappedBuffer(new byte[] {_TAG_T1_114_2000});
	private static final ByteBuf T1_COMMON=Unpooled.wrappedBuffer(new byte[] {(byte)(_TAG_T1_114_1996 + _TAG_T1_114_2000)});
    
	private Boolean has1996=true;
	private Boolean has2000=true;
	
	/**
     * Creating ProtocolVersion that support both T1_114_1996Supported and T1_114_2000Supported
     */
    public ProtocolVersionImpl() {
    	setValue(Unpooled.wrappedBuffer(T1_COMMON));        
    }

    public boolean isT1_114_1996Supported() {
    	return has1996;
    }

    public boolean isT1_114_2000Supported() {
    	return has2000;
    }

    public boolean isSupportedVersion() {
    	return has1996 || has2000;
    }

    public void setT1_114_1996Supported(boolean val) {
    	has1996=val;
    	if(val) {
	    	if(has2000)
	    		setValue(Unpooled.wrappedBuffer(T1_COMMON));
	    	else
	    		setValue(Unpooled.wrappedBuffer(T1_1996));
    	} else {
    		if(has2000)
	    		setValue(Unpooled.wrappedBuffer(T1_2000));
	    	else
	    		setValue(Unpooled.wrappedBuffer(T1_EMPTY));
    	}    
    }

    public void setT1_114_2000Supported(boolean val) {
    	has2000=val;
    	if(val) {
	    	if(has1996)
	    		setValue(Unpooled.wrappedBuffer(T1_COMMON));
	    	else
	    		setValue(Unpooled.wrappedBuffer(T1_2000));
    	} else {
    		if(has1996)
	    		setValue(Unpooled.wrappedBuffer(T1_1996));
	    	else
	    		setValue(Unpooled.wrappedBuffer(T1_EMPTY));
    	}        
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
