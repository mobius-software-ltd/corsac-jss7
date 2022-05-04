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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AuthenticationQuintupletImpl implements AuthenticationQuintuplet {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString xres;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private ASNOctetString ck;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=3)
	private ASNOctetString ik;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=4)
	private ASNOctetString autn;

    public AuthenticationQuintupletImpl() {
    }

    public AuthenticationQuintupletImpl(ByteBuf rand, ByteBuf xres, ByteBuf ck, ByteBuf ik, ByteBuf autn) {
    	
    	if(rand!=null)
    		this.rand = new ASNOctetString(rand,"RAND",16,16,false);
    	
    	if(xres!=null)
    		this.xres = new ASNOctetString(xres,"XRES",4,16,false);
    	
    	if(ck!=null)
    		this.ck = new ASNOctetString(ck,"CK",16,16,false);
    	
    	if(ik!=null)
    		this.ik = new ASNOctetString(ik,"IK",16,16,false);
    	
    	if(autn!=null)
    		this.autn = new ASNOctetString(autn,"AUTN",16,16,false);    	
    }

    public ByteBuf getRand() {
    	if(this.rand==null)
    		return null;
    	
    	return this.rand.getValue();    	
    }

    public ByteBuf getXres() {
    	if(this.xres==null)
    		return null;
    	
    	return this.xres.getValue();    	
    }

    public ByteBuf getCk() {
    	if(this.ck==null)
    		return null;
    	
    	return this.ck.getValue();    	
    }

    public ByteBuf getIk() {
    	if(this.ik==null)
    		return null;
    	
    	return this.ik.getValue();    	
    }

    public ByteBuf getAutn() {
    	if(this.autn==null)
    		return null;
    	
    	return this.autn.getValue();    	
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationQuintuplet [");

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(rand.printDataArr());
            sb.append("], ");
        }
        if (this.xres != null) {
            sb.append("xres=[");
            sb.append(xres.printDataArr());
            sb.append("], ");
        }
        if (this.ck != null) {
            sb.append("ck=[");
            sb.append(ck.printDataArr());
            sb.append("], ");
        }
        if (this.ik != null) {
            sb.append("ik=[");
            sb.append(ik.printDataArr());
            sb.append("], ");
        }
        if (this.autn != null) {
            sb.append("autn=[");
            sb.append(autn.printDataArr());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(rand==null)
			throw new ASNParsingComponentException("rand should be set for authentication quintuplet", ASNParsingComponentExceptionReason.MistypedParameter);

		if(xres==null)
			throw new ASNParsingComponentException("xres should be set for authentication quintuplet", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(ck==null)
			throw new ASNParsingComponentException("ck should be set for authentication quintuplet", ASNParsingComponentExceptionReason.MistypedParameter);

		if(ik==null)
			throw new ASNParsingComponentException("ik should be set for authentication quintuplet", ASNParsingComponentExceptionReason.MistypedParameter);

		if(autn==null)
			throw new ASNParsingComponentException("autn should be set for authentication quintuplet", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}