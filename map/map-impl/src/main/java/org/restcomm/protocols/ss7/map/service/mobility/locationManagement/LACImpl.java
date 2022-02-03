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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LAC;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class LACImpl extends ASNOctetString implements LAC {
	public LACImpl() {
		super("LAC",2,2,false);
    }

	public LACImpl(int lac) throws MAPException {
		super(translate(lac),"LAC",2,2,false);
	}
	
    public static ByteBuf translate(int lac) throws MAPException {
        ByteBuf data = Unpooled.buffer(2);
        data.writeByte((byte) (lac / 256));
        data.writeByte((lac % 256));
        return data;
    }

    public int getLac() throws MAPException {
    	ByteBuf value=getValue();
        if (value == null)
            throw new MAPException("Data must not be empty");
        if (value.readableBytes() != 2)
            throw new MAPException("Data length must be equal 5");

        int res = (value.readByte() & 0xFF) * 256 + (value.readByte() & 0xFF);
        return res;
    }

    @Override
    public String toString() {

        int lac = 0;
        boolean goodData = false;

        try {
            lac = this.getLac();
            goodData = true;
        } catch (MAPException e) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LAC [");
        if (goodData) {
            sb.append("Lac=");
            sb.append(lac);
        } else {
            sb.append("Data=");
            sb.append(printDataArr());
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        if(getValue()!=null)
        	result = prime * result + ByteBufUtil.hashCode(getValue());
        
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
        LACImpl other = (LACImpl) obj;
        ByteBuf value=getValue();
        ByteBuf otherValue=other.getValue();
        if(value==null) {
        	if(otherValue!=null)
        		return false;
        }
        else {
        	if(otherValue==null)
        		return false;
        	
        	if (!ByteBufUtil.equals(value, otherValue))
        		return false;
        }
        
        return true;
    }
}
