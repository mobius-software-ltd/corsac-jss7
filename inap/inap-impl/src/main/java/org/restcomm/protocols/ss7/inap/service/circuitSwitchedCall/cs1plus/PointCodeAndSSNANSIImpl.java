/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.PointCodeAndSSNANSI;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class PointCodeAndSSNANSIImpl extends ASNOctetString implements PointCodeAndSSNANSI {
	public PointCodeAndSSNANSIImpl() {
    }

    public PointCodeAndSSNANSIImpl(Integer network,Integer cluster,Integer member,Integer ssn) {
    	if(network!=null || cluster!=null || member!=null || ssn!=null) {
    		byte[] value=new byte[4];
    		if(network!=null)
    			value[0]=network.byteValue();
    		
    		if(cluster!=null)
    			value[1]=cluster.byteValue();
    		
    		if(member!=null)
    			value[2]=member.byteValue();
    		
    		if(ssn!=null)
    			value[3]=ssn.byteValue();
    		
    		setValue(Unpooled.wrappedBuffer(value));    	
    	}
    }

    public Integer getNetwork() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 4)
            return null;

        return data.readByte() & 0x0FF;
    }

    public Integer getCluster() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 4)
            return null;

        data.skipBytes(1);
        return data.readByte() & 0x0FF;
    }

    public Integer getMember() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 4)
            return null;

        data.skipBytes(2);
        return data.readByte() & 0x0FF;
    }

    public Integer getSSN() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 4)
            return null;

        data.skipBytes(3);
        return data.readByte() & 0x0FF;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("PointCodeAndSSNANSI [");
        
        Integer network=getNetwork();
        if (network != null) {
            sb.append("network=");
            sb.append(network);            
        }
        
        Integer cluster=getCluster();
        if (cluster != null) {
            sb.append("cluster=");
            sb.append(cluster);            
        }
        
        Integer member=getMember();
        if (member != null) {
            sb.append("member=");
            sb.append(member);            
        }
        
        Integer ssn=getSSN();
        if (ssn != null) {
            sb.append("ssn=");
            sb.append(ssn);            
        }
        
        sb.append("]");

        return sb.toString();
    }
}
