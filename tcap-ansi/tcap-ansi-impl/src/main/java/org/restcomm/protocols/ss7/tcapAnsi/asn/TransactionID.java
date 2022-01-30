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

import java.util.concurrent.ConcurrentHashMap;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNPostprocess;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=7,constructed=false,lengthIndefinite=false)
@ASNPostprocess
public class TransactionID {
    private ByteBuf firstElem;
    private ByteBuf secondElem;

    public ByteBuf getFirstElem() {
    	if(firstElem!=null)
    		return Unpooled.wrappedBuffer(firstElem);
    	
    	return null;
    }

    public void setFirstElem(ByteBuf firstElem) {
        this.firstElem = firstElem;
    }

    public ByteBuf getSecondElem() {
    	if(secondElem!=null)
    		return Unpooled.wrappedBuffer(secondElem);
    	
    	return null;
    }

    public void setSecondElem(ByteBuf secondElem) {
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
	public Boolean decode(ASNParser parser, Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) {
		if(buffer.readableBytes()>=4)
			firstElem=buffer.readSlice(4);
					
		
		if(buffer.readableBytes()>=4)
			secondElem=buffer.readSlice(4);
		
		return false;
	}
}
