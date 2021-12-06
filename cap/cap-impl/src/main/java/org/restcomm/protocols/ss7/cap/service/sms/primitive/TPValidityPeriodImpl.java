/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ValidityPeriodImpl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TPValidityPeriodImpl extends ASNOctetString implements TPValidityPeriod {
	public TPValidityPeriodImpl() {
    }

    public TPValidityPeriodImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public TPValidityPeriodImpl(int relativeFormat) {
    	byte[] data = new byte[] { (byte) relativeFormat };
    	setValue(Unpooled.wrappedBuffer(data));
    }

    public TPValidityPeriodImpl(AbsoluteTimeStampImpl absoluteFormatValue) {
        ByteBuf buffer=Unpooled.buffer(7);
        try {
            absoluteFormatValue.encodeData(buffer);
        } catch (MAPException e) {
            // This can not occur
        }
        setValue(buffer);
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public ValidityPeriodImpl getValidityPeriod() throws CAPException {
    	ByteBuf value=getValue();
        if (value == null)
            throw new CAPException("Error when getting ValidityPeriod: data must not be null");

        // we support RelativeFormat and AbsoluteFormat
        if (value.readableBytes() != 1 && value.readableBytes() != 7)
            throw new CAPException("Error when getting ValidityPeriod: data must has length 1 or 7, found " + value.readableBytes());

        ValidityPeriodImpl vp;
        if (value.readableBytes() == 1) {
            vp = new ValidityPeriodImpl(value.readByte());
        } else {
            AbsoluteTimeStampImpl absoluteFormatValue;
            try {
                absoluteFormatValue = AbsoluteTimeStampImpl.createMessage(value);
            } catch (MAPException e) {
                throw new CAPException("MAPException when AbsoluteTimeStampImpl creating: " + e.getMessage(), e);
            }
            vp = new ValidityPeriodImpl(absoluteFormatValue);
        }
        return vp;
    }

    @Override
    public String toString() {

        try {
            ValidityPeriodImpl vp = this.getValidityPeriod();
            return "TPValidityPeriod [" + vp + "]";
        } catch (CAPException e) {
            return super.toString();
        }
    }

}
