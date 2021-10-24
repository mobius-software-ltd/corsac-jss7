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

package org.restcomm.protocols.ss7.map.api.service.mobility.authentication;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AuthenticationTripletImpl {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString sres;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private ASNOctetString kc;

    public AuthenticationTripletImpl() {
    }

    public AuthenticationTripletImpl(byte[] rand, byte[] sres, byte[] kc) {

    	if(rand!=null) {
    		this.rand = new ASNOctetString();
    		this.rand.setValue(Unpooled.wrappedBuffer(rand));
    	}
        
    	if(sres!=null) {
    		this.sres = new ASNOctetString();
    		this.sres.setValue(Unpooled.wrappedBuffer(sres));
    	}
    	
    	if(kc!=null) {
    		this.kc = new ASNOctetString();
    		this.kc.setValue(Unpooled.wrappedBuffer(kc));
    	}
    }

    public byte[] getRand() {
    	if(this.rand==null)
    		return null;
    	
    	ByteBuf value=this.rand.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public byte[] getSres() {
    	if(this.sres==null)
    		return null;
    	
    	ByteBuf value=this.sres.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public byte[] getKc() {
    	if(this.kc==null)
    		return null;
    	
    	ByteBuf value=this.kc.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationTriplet [");

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(ASNOctetString.printDataArr(getRand()));
            sb.append("], ");
        }
        if (this.sres != null) {
            sb.append("sres=[");
            sb.append(ASNOctetString.printDataArr(getSres()));
            sb.append("], ");
        }
        if (this.kc != null) {
            sb.append("kc=[");
            sb.append(ASNOctetString.printDataArr(getKc()));
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
