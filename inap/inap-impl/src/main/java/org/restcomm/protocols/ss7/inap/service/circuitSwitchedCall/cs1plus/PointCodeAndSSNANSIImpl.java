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
		super("PointCodeAndSSNANSI",4,4,false);
    }

	public PointCodeAndSSNANSIImpl(Integer network,Integer cluster,Integer member,Integer ssn) {
		super(translate(network, cluster, member, ssn),"PointCodeAndSSNANSI",4,4,false);
	}
	
    public static ByteBuf translate(Integer network,Integer cluster,Integer member,Integer ssn) {
    	if(network!=null || cluster!=null || member!=null || ssn!=null) {
    		ByteBuf value=Unpooled.buffer(4);
    		if(network!=null)
    			value.writeByte(network.byteValue());
    		else
    			value.writeByte(0);
    		
    		if(cluster!=null)
    			value.writeByte(cluster.byteValue());
    		else
        		value.writeByte(0);
        		
    		if(member!=null)
    			value.writeByte(member.byteValue());
    		else
        		value.writeByte(0);
        			
    		if(ssn!=null)
    			value.writeByte(ssn.byteValue());
    		else
        		value.writeByte(0);
        			
    		return value;	
    	}
    	
    	return null;
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
