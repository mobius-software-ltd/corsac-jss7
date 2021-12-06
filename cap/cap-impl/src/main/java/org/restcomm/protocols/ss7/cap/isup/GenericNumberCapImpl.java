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

package org.restcomm.protocols.ss7.cap.isup;

import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 * @author tamas gyorgyey
 */
public class GenericNumberCapImpl extends ASNOctetString implements GenericNumberCap {
	public GenericNumberCapImpl() {
    }

    public GenericNumberCapImpl(byte[] data) {
       setValue(Unpooled.wrappedBuffer(data));
    }

    public GenericNumberCapImpl(GenericNumber genericNumber) throws CAPException {
        setGenericNumber(genericNumber);
    }

    public void setGenericNumber(GenericNumber genericNumber) throws CAPException {
        if (genericNumber == null)
            throw new CAPException("The genericNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((GenericNumberImpl) genericNumber).encode(buffer);
            setValue(buffer);
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding genericNumber: " + e.getMessage(), e);
        }
    }

    public byte[] getData() {
    	ByteBuf buffer=getValue();
    	if(buffer==null)
    		return null;
    	
    	byte[] data=new byte[buffer.readableBytes()];
    	buffer.readBytes(data);
        return data;
    }

    public GenericNumber getGenericNumber() throws CAPException {
        if (this.getValue() == null)
            throw new CAPException("The data has not been filled");

        try {
            GenericNumberImpl ocn = new GenericNumberImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding GenericNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericNumberCap [");
        
        byte[] data=this.getData();
        if (data != null) {
            sb.append("data=[");
            sb.append(printDataArr(data));
            sb.append("]");
            try {
                GenericNumber gn = this.getGenericNumber();
                sb.append(", ");
                sb.append(gn.toString());
            } catch (CAPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
