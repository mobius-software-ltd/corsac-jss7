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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AuthenticationTripletImpl implements AuthenticationTriplet {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString sres;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private ASNOctetString kc;

    public AuthenticationTripletImpl() {
    }

    public AuthenticationTripletImpl(ByteBuf rand, ByteBuf sres, ByteBuf kc) {

    	if(rand!=null)
    		this.rand = new ASNOctetString(rand);
    	
    	if(sres!=null)
    		this.sres = new ASNOctetString(sres);
    	
    	if(kc!=null)
    		this.kc = new ASNOctetString(kc);
    }

    public ByteBuf getRand() {
    	if(this.rand==null)
    		return null;
    	
    	return this.rand.getValue();    	
    }

    public ByteBuf getSres() {
    	if(this.sres==null)
    		return null;
    	
    	return this.sres.getValue();    	
    }

    public ByteBuf getKc() {
    	if(this.kc==null)
    		return null;
    	
    	return this.kc.getValue();    	
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationTriplet [");

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(rand.printDataArr());
            sb.append("], ");
        }
        if (this.sres != null) {
            sb.append("sres=[");
            sb.append(sres.printDataArr());
            sb.append("], ");
        }
        if (this.kc != null) {
            sb.append("kc=[");
            sb.append(kc.printDataArr());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
