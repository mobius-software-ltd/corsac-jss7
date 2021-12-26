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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericName;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class GenericNameImpl extends ASNOctetString implements GenericName {
	public GenericNameImpl() {
    }

    public GenericNameImpl(byte[] data) {
    	if(data!=null) {
    		setValue(Unpooled.wrappedBuffer(data));
    	}
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
        if (value == null)
            return null;

        byte[] data=new byte[value.readableBytes()];
        value.readBytes(data);
        return data;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("GenericName [");
        byte[] data=getData();
        if (data != null) {
            sb.append("data=");
            sb.append(ASNOctetString.printDataArr(data));            
        }
        sb.append("]");

        return sb.toString();
    }
}
