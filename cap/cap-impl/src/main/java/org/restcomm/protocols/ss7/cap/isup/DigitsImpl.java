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
import org.restcomm.protocols.ss7.cap.api.isup.Digits;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class DigitsImpl extends ASNOctetString implements Digits {
	private boolean isGenericDigits;
    private boolean isGenericNumber;

    public DigitsImpl() {
    }

    public DigitsImpl(byte[] data) {
    	setValue(Unpooled.wrappedBuffer(data));        
    }

    public DigitsImpl(GenericDigits genericDigits) throws CAPException {
        this.setGenericDigits(genericDigits);
    }

    public DigitsImpl(GenericNumber genericNumber) throws CAPException {
        this.setGenericNumber(genericNumber);
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public GenericDigits getGenericDigits() throws CAPException {
        if (this.getValue() == null)
            throw new CAPException("The data has not been filled");
        if (!this.isGenericDigits)
            throw new CAPException("Primitive is not marked as GenericDigits (use setGenericDigits() before)");

        try {
            GenericDigitsImpl ocn = new GenericDigitsImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding GenericDigits: " + e.getMessage(), e);
        }
    }

    public GenericNumber getGenericNumber() throws CAPException {
        if (this.getValue() == null)
            throw new CAPException("The data has not been filled");
        if (!this.isGenericNumber)
            throw new CAPException("Primitive is not marked as GenericNumber (use setGenericNumber() before)");

        try {
            GenericNumberImpl ocn = new GenericNumberImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when decoding GenericNumber: " + e.getMessage(), e);
        }
    }

    public void setData(byte[] data) {
    	setValue(Unpooled.wrappedBuffer(data));
    }

    public void setGenericDigits(GenericDigits genericDigits) throws CAPException {

        if (genericDigits == null)
            throw new CAPException("The genericDigits parameter must not be null");
        try {
        	ByteBuf value=Unpooled.buffer();
        	((GenericDigitsImpl) genericDigits).encode(value);
        	setValue(value);
            setIsGenericDigits();
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding genericDigits: " + e.getMessage(), e);
        }
    }

    public void setGenericNumber(GenericNumber genericNumber) throws CAPException {

        if (genericNumber == null)
            throw new CAPException("The genericNumber parameter must not be null");
        try {
        	ByteBuf value=Unpooled.buffer();
        	((GenericNumberImpl) genericNumber).encode(value);
        	setValue(value);
            setIsGenericNumber();
        } catch (ParameterException e) {
            throw new CAPException("ParameterException when encoding genericNumber: " + e.getMessage(), e);
        }
    }

    public boolean getIsGenericDigits() {
        return isGenericDigits;
    }

    public boolean getIsGenericNumber() {
        return isGenericNumber;
    }

    public void setIsGenericDigits() {
        isGenericDigits = true;
        isGenericNumber = false;
    }

    public void setIsGenericNumber() {
        isGenericNumber = true;
        isGenericDigits = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Digits [");

        byte[] data=this.getData();
        if (data != null) {
            sb.append("data=[");
            sb.append(printDataArr(data));
            sb.append("]");
            try {
                if (this.getIsGenericNumber()) {
                    GenericNumber gn = this.getGenericNumber();
                    sb.append(", genericNumber");
                    sb.append(gn.toString());
                }

                if (this.getIsGenericDigits()) {
                    GenericDigits gd = this.getGenericDigits();
                    sb.append(", genericDigits");
                    sb.append(gd.toString());
                }
            } catch (CAPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
