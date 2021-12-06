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
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class LocationNumberCapImpl extends ASNOctetString implements LocationNumberCap {
	public LocationNumberCapImpl() {
    }

    public LocationNumberCapImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public LocationNumberCapImpl(LocationNumber locationNumber) throws CAPException {
        setLocationNumber(locationNumber);
    }

    public void setLocationNumber(LocationNumber locationNumber) throws CAPException {
        if (locationNumber == null)
            throw new CAPException("The locationNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((LocationNumberImpl) locationNumber).encode(buffer);
            setValue(buffer);
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding locationNumber: " + e.getMessage(), e);
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

    public LocationNumber getLocationNumber() throws CAPException {
        if (this.getValue() == null)
            throw new CAPException("The data has not been filled");

        try {
            LocationNumberImpl ln = new LocationNumberImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding LocationNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationNumberCap [");

        byte[] data=this.getData();
        if (data != null) {
            sb.append("data=[");
            sb.append(printDataArr(data));
            sb.append("]");
            try {
                LocationNumber ln = this.getLocationNumber();
                sb.append(", ");
                sb.append(ln.toString());
            } catch (CAPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
