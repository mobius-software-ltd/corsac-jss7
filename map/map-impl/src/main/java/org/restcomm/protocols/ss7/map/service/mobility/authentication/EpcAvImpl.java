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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EpcAvImpl implements EpcAv {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString2 rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString2 xres;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private ASNOctetString2 autn;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=3)
	private ASNOctetString2 kasme;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    public EpcAvImpl() {
    }

    public EpcAvImpl(ByteBuf rand, ByteBuf xres, ByteBuf autn, ByteBuf kasme, MAPExtensionContainer extensionContainer) {

    	if(rand!=null)
    		this.rand = new ASNOctetString2(rand);
    	
    	if(xres!=null)
    		this.xres = new ASNOctetString2(xres);
    	
    	if(autn!=null)
    		this.autn = new ASNOctetString2(autn);
    	
    	if(kasme!=null)
    		this.kasme = new ASNOctetString2(kasme);
    	
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
            sb.append(ASNOctetString.printDataArr(getRand()));
            sb.append("], ");
        }
        if (this.xres != null) {
            sb.append("xres=[");
            sb.append(ASNOctetString.printDataArr(getXres()));
            sb.append("], ");
        }
        if (this.autn != null) {
            sb.append("autn=[");
            sb.append(ASNOctetString.printDataArr(getAutn()));
            sb.append("]");
        }
        if (this.kasme != null) {
            sb.append("kasme=[");
            sb.append(ASNOctetString.printDataArr(getKasme()));
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
}
