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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.PointCodeAndSSN;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class PointCodeAndSSNImpl extends ASNOctetString implements PointCodeAndSSN {
	public PointCodeAndSSNImpl() {
		super("PointCodeAndSSN",3,3,false);
    }

	public PointCodeAndSSNImpl(Integer spc,Integer ssn) {
		super(translate(spc, ssn),"PointCodeAndSSN",3,3,false);
	}
	
    public static ByteBuf translate(Integer spc,Integer ssn) {
    	if(spc!=null || ssn!=null) {
    		ByteBuf result=Unpooled.buffer(3);
    		if(spc!=null)
    			result.writeShort(spc);
    		else
    			result.writeShort(0);
    		
    		if(ssn!=null)
    			result.writeByte(ssn);
    		
    		return result;    	
    	}
    	
    	return null;
    }

    public Integer getSPC() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 3)
            return null;

        return data.readUnsignedShort();
    }

    public Integer getSSN() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 3)
            return null;

        data.skipBytes(2);
        return data.readByte() & 0x0FF;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("PointCodeAndSSN [");
        
        Integer spc=getSPC();
        if (spc != null) {
            sb.append("spc=");
            sb.append(spc);            
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
