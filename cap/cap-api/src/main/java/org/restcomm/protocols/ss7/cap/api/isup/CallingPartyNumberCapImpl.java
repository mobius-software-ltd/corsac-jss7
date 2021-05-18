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

package org.restcomm.protocols.ss7.cap.api.isup;

import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class CallingPartyNumberCapImpl extends ASNOctetString {
	public CallingPartyNumberCapImpl() {
    }

    public CallingPartyNumberCapImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public CallingPartyNumberCapImpl(CallingPartyNumber callingPartyNumber) throws CAPException {
        setCallingPartyNumber(callingPartyNumber);
    }

    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) throws CAPException {
        if (callingPartyNumber == null)
            throw new CAPException("The callingPartyNumber parameter must not be null");
        try {
        	ByteBuf buf=Unpooled.buffer();
        	((CallingPartyNumberImpl) callingPartyNumber).encode(buf);
            setValue(buf);
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding callingPartyNumber: " + e.getMessage(), e);
        }
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public CallingPartyNumber getCallingPartyNumber() throws CAPException {
        if (this.getValue() == null)
            throw new CAPException("The data has not been filled");

        try {
            CallingPartyNumberImpl ln = new CallingPartyNumberImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding CallingPartyNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallingPartyNumberCap [");

        byte[] data=this.getData();
        if (data != null) {
            sb.append("data=[");
            sb.append(printDataArr(data));
            sb.append("]");
            try {
                CallingPartyNumber cpn = this.getCallingPartyNumber();
                sb.append(", ");
                sb.append(cpn.toString());
            } catch (CAPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
