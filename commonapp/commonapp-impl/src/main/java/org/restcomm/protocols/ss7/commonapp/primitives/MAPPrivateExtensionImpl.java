/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.primitives;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPPrivateExtension;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPPrivateExtensionImpl implements MAPPrivateExtension {
	@ASNExclude
	private ASNObjectIdentifier oId;
	
	private ByteBuf data;

    public MAPPrivateExtensionImpl() {
    }

    public MAPPrivateExtensionImpl(List<Long> oId, ByteBuf data) {
        this.oId = new ASNObjectIdentifier(oId);
        if(data!=null)
        	this.data=Unpooled.wrappedBuffer(data);        
    }

	@ASNLength
	public Integer getLength(ASNParser parser) throws ASNException {
		Integer totalLength=0;		
		if(oId!=null)
			totalLength=parser.getLength(oId);
		
		if(data!=null)
			totalLength+=data.readableBytes();	
		
		return totalLength;
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws ASNException {
		if(oId!=null)
			parser.encode(buffer, oId);			
		
		if(data!=null) {
			data.markReaderIndex();
			data.markWriterIndex();
			buffer.writeBytes(data);
			data.resetReaderIndex();
			data.resetWriterIndex();
		}
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) throws ASNException {
		ASNDecodeResult oidResult=parser.decode(buffer);
		if(!oidResult.getHadErrors() && (oidResult.getResult() instanceof ASNObjectIdentifier))
			this.oId=(ASNObjectIdentifier)oidResult.getResult();
		
		if(buffer.readableBytes()>0)
			data=Unpooled.wrappedBuffer(buffer);
		else
			data=null;
		
		return false;
	}
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#getOId()
     */
    public List<Long> getOId() {
    	if(this.oId==null)
    		return null;
    	
        return this.oId.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#setOId (long[])
     */
    public void setOId(List<Long> oId) {
        this.oId = new ASNObjectIdentifier(oId);        
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#getData()
     */
    public ByteBuf getData() {
    	if(this.data==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(this.data);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.dialog.MAPPrivateExtension#setData (ByteBuf)
     */
    public void setData(ByteBuf data) {
    	if(data==null)
    		this.data = null;
    	else
    		this.data=Unpooled.wrappedBuffer(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PrivateExtension [");

        if (this.oId != null && this.oId.getValue()!=null && this.oId.getValue().size() > 0) {
            sb.append("Oid=");
            sb.append(this.ListToString(this.oId.getValue()));
        }

        if (this.data != null) {
            sb.append(", data=");
            sb.append(ASNOctetString.printDataArr(getData()));
        }

        sb.append("]");

        return sb.toString();
    }

    private String ListToString(List<Long> array) {
        StringBuilder sb = new StringBuilder();
        int i1 = 0;
        for (Long b : array) {
            if (i1 == 0)
                i1 = 1;
            else
                sb.append(", ");
            sb.append(b);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (data==null?0:data.hashCode());
        if(oId!=null && oId.getValue()!=null)
        result = prime * result + oId.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MAPPrivateExtensionImpl other = (MAPPrivateExtensionImpl) obj;
        if(data==null) {
        	if(other.data!=null)
        		return false;
        }
        else if (data.equals(other.data))
            return false;
        
        if(oId==null || oId.getValue()==null) {
        	if(other.oId!=null && other.oId.getValue()!=null)
        		return false;
        }
        else if (oId.getValue().equals(other.oId.getValue()))
            return false;
        
        return true;
    }
}