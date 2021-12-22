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

package org.restcomm.protocols.ss7.commonapp.isup;

import org.restcomm.protocols.ss7.commonapp.api.APPException;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
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
public class CallingPartyNumberIsupImpl extends ASNOctetString implements CallingPartyNumberIsup {
	public CallingPartyNumberIsupImpl() {
    }

    public CallingPartyNumberIsupImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public CallingPartyNumberIsupImpl(CallingPartyNumber callingPartyNumber) throws APPException {
        setCallingPartyNumber(callingPartyNumber);
    }

    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) throws APPException {
        if (callingPartyNumber == null)
            throw new APPException("The callingPartyNumber parameter must not be null");
        try {
        	ByteBuf buf=Unpooled.buffer();
        	((CallingPartyNumberImpl) callingPartyNumber).encode(buf);
            setValue(buf);
        } catch (ParameterException e) {
            throw new APPException("ParameterException when encoding callingPartyNumber: " + e.getMessage(), e);
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

    public CallingPartyNumber getCallingPartyNumber() throws APPException {
        if (this.getValue() == null)
            throw new APPException("The data has not been filled");

        try {
            CallingPartyNumberImpl ln = new CallingPartyNumberImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new APPException("ParameterException when decoding CallingPartyNumber: " + e.getMessage(), e);
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
            } catch (APPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
