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
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ForwardCallIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class ForwardCallIndicatorsIsupImpl extends ASNOctetString implements ForwardCallIndicatorsIsup {
	public ForwardCallIndicatorsIsupImpl() {
    }

    public ForwardCallIndicatorsIsupImpl(byte[] data) {
    	setValue(Unpooled.wrappedBuffer(data));
    }

    public ForwardCallIndicatorsIsupImpl(ForwardCallIndicators forwardCallIndicators) throws APPException {
        setForwardCallIndicators(forwardCallIndicators);
    }

    public void setForwardCallIndicators(ForwardCallIndicators forwardCallIndicators) throws APPException {
        if (forwardCallIndicators == null)
            throw new APPException("The forwardCallIndicators parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((ForwardCallIndicatorsImpl) forwardCallIndicators).encode(buffer);
            setValue(buffer);
        } catch (ParameterException e) {
            throw new APPException("ParameterException when encoding originalCalledNumber: " + e.getMessage(), e);
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

    public ForwardCallIndicators getForwardCallIndicators() throws APPException {
        if (this.getValue() == null)
            throw new APPException("The data has not been filled");

        try {
        	ForwardCallIndicatorsImpl ocn = new ForwardCallIndicatorsImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new APPException("ParameterException when decoding OriginalCalledNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardCallIndicatorsIsup [");

        byte[] data=this.getData();
        if (data != null) {
            sb.append("data=[");
            sb.append(printDataArr(data));
            sb.append("]");
            try {
                ForwardCallIndicators fci = this.getForwardCallIndicators();
                sb.append(", ");
                sb.append(fci.toString());
            } catch (APPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
