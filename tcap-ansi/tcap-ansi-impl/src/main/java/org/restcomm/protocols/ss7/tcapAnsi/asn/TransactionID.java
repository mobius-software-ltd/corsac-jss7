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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import io.netty.buffer.ByteBuf;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=7,constructed=false,lengthIndefinite=false)
public class TransactionID {
    private byte[] firstElem;
    private byte[] secondElem;

    public byte[] getFirstElem() {
        return firstElem;
    }

    public void setFirstElem(byte[] firstElem) {
        this.firstElem = firstElem;
    }

    public byte[] getSecondElem() {
        return secondElem;
    }

    public void setSecondElem(byte[] secondElem) {
        this.secondElem = secondElem;
    }
    
    @ASNLength
	public Integer getLength(ASNParser parser) {
		if(firstElem!=null) {
			if(secondElem!=null)
				return 8;
			
			return 4;
		}
		
		return 0;
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) {		
		if(firstElem!=null)
			buffer.writeBytes(firstElem);
		
		if(secondElem!=null)
			buffer.writeBytes(secondElem);
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser, Object parent,ByteBuf buffer,Boolean skipErrors) {
		if(buffer.readableBytes()>=4) {
			firstElem=new byte[4];
			buffer.readBytes(firstElem);
		}			
		
		if(buffer.readableBytes()>=4) {
			secondElem=new byte[4];
			buffer.readBytes(secondElem);
		}			
		
		return false;
	}
}
