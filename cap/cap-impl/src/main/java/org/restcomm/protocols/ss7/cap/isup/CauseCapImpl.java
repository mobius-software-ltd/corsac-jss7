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
import org.restcomm.protocols.ss7.cap.api.isup.CauseCap;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class CauseCapImpl extends ASNOctetString implements CauseCap {
	public CauseCapImpl() {
    }

    public CauseCapImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public CauseCapImpl(CauseIndicators causeIndicators) throws CAPException {
        setCauseIndicators(causeIndicators);
    }

    public void setCauseIndicators(CauseIndicators causeIndicators) throws CAPException {
        if (causeIndicators == null)
            throw new CAPException("The causeIndicators parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((CauseIndicatorsImpl) causeIndicators).encode(buffer);
            setValue(buffer);
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding causeIndicators: " + e.getMessage(), e);
        }
    }

    public byte[] getData() {
    	ByteBuf buffer=this.getValue();
    	if(buffer==null)
    		return null;
    	
    	byte[] data=new byte[buffer.readableBytes()];
    	buffer.readBytes(data);
        return data;
    }

    public CauseIndicators getCauseIndicators() throws CAPException {
        if (this.getValue() == null)
            throw new CAPException("The data has not been filled");

        try {
            CauseIndicatorsImpl ln = new CauseIndicatorsImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding locationNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CauseCap [");

        byte[] data=this.getData();
        if (data != null) {
            sb.append("data=[");
            sb.append(printDataArr(data));
            sb.append("]");
            try {
                CauseIndicators ci = this.getCauseIndicators();
                sb.append(", ");
                sb.append(ci.toString());
            } catch (CAPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
