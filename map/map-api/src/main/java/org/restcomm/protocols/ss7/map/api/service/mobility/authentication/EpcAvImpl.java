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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

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
public class EpcAvImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString xres;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private ASNOctetString autn;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=3)
	private ASNOctetString kasme;
	
    private MAPExtensionContainerImpl extensionContainer;

    public EpcAvImpl() {
    }

    public EpcAvImpl(byte[] rand, byte[] xres, byte[] autn, byte[] kasme, MAPExtensionContainerImpl extensionContainer) {

    	if(rand!=null) {
    		this.rand = new ASNOctetString();
    		this.rand.setValue(Unpooled.wrappedBuffer(rand));
    	}
    	
    	if(xres!=null) {
    		this.xres = new ASNOctetString();
    		this.xres.setValue(Unpooled.wrappedBuffer(xres));
    	}
    	
    	if(autn!=null) {
    		this.autn = new ASNOctetString();
    		this.autn.setValue(Unpooled.wrappedBuffer(autn));
    	} 
    	
    	if(kasme!=null) {
    		this.kasme = new ASNOctetString();
    		this.kasme.setValue(Unpooled.wrappedBuffer(kasme));
    	}
    	
        this.extensionContainer = extensionContainer;
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

    public byte[] getXres() {
    	if(this.xres==null)
    		return null;
    	
    	ByteBuf value=this.xres.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public byte[] getAutn() {
    	if(this.autn==null)
    		return null;
    	
    	ByteBuf value=this.autn.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public byte[] getKasme() {
    	if(this.kasme==null)
    		return null;
    	
    	ByteBuf value=this.kasme.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EpcAv [");

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(this.rand.printDataArr(getRand()));
            sb.append("], ");
        }
        if (this.xres != null) {
            sb.append("xres=[");
            sb.append(this.rand.printDataArr(getXres()));
            sb.append("], ");
        }
        if (this.autn != null) {
            sb.append("autn=[");
            sb.append(this.rand.printDataArr(getAutn()));
            sb.append("]");
        }
        if (this.kasme != null) {
            sb.append("kasme=[");
            sb.append(this.rand.printDataArr(getKasme()));
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
