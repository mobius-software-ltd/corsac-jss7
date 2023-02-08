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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;

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
public class EpcAvImpl implements EpcAv {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString xres;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private ASNOctetString autn;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=3)
	private ASNOctetString kasme;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    public EpcAvImpl() {
    }

    public EpcAvImpl(ByteBuf rand, ByteBuf xres, ByteBuf autn, ByteBuf kasme, MAPExtensionContainer extensionContainer) {

    	if(rand!=null)
    		this.rand = new ASNOctetString(rand,"RAND",16,16,false);
    	
    	if(xres!=null)
    		this.xres = new ASNOctetString(xres,"XRES",4,16,false);
    	
    	if(autn!=null)
    		this.autn = new ASNOctetString(autn,"AUTN",16,16,false);
    	
    	if(kasme!=null)
    		this.kasme = new ASNOctetString(kasme,"KASME",32,32,false);
    	
        this.extensionContainer = extensionContainer;
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

    public ByteBuf getAutn() {
    	if(this.autn==null)
    		return null;
    	
    	return this.autn.getValue();    	
    }

    public ByteBuf getKasme() {
    	if(this.kasme==null)
    		return null;
    	
    	return this.kasme.getValue();    	
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EpcAv [");

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
        if (this.autn != null) {
            sb.append("autn=[");
            sb.append(autn.printDataArr());
            sb.append("]");
        }
        if (this.kasme != null) {
            sb.append("kasme=[");
            sb.append(kasme.printDataArr());
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=[");
            sb.append(this.extensionContainer);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(rand==null)
			throw new ASNParsingComponentException("rand should be set for epc av", ASNParsingComponentExceptionReason.MistypedParameter);

		if(xres==null)
			throw new ASNParsingComponentException("xres should be set for epc av", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(autn==null)
			throw new ASNParsingComponentException("autn should be set for epc av", ASNParsingComponentExceptionReason.MistypedParameter);

		if(kasme==null)
			throw new ASNParsingComponentException("kasme should be set for epc av", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
