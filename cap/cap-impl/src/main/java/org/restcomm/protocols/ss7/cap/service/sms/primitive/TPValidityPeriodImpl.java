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
import org.restcomm.protocols.ss7.commonapp.api.APPException;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.commonapp.smstpu.ValidityPeriodImpl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TPValidityPeriodImpl extends ASNOctetString2 implements TPValidityPeriod {
	public TPValidityPeriodImpl() {
    }

    public TPValidityPeriodImpl(int relativeFormat) {
    	super(translate(relativeFormat));
    }
    
    private static ByteBuf translate(int relativeFormat) {
    	ByteBuf value=Unpooled.buffer(1);
    	value.writeByte(relativeFormat);
    	return value;
    }

    public TPValidityPeriodImpl(AbsoluteTimeStamp absoluteFormatValue) {
    	super(translate(absoluteFormatValue));
    }
    
    private static ByteBuf translate(AbsoluteTimeStamp absoluteFormatValue) {    	
        ByteBuf buffer=Unpooled.buffer(7);
        try {
            absoluteFormatValue.encodeData(buffer);
        } catch (APPException e) {
            // This can not occur
        }
        return buffer;
    }

    public ValidityPeriod getValidityPeriod() throws CAPException {
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
            } catch (APPException e) {
                throw new CAPException("MAPException when AbsoluteTimeStampImpl creating: " + e.getMessage(), e);
            }
            vp = new ValidityPeriodImpl(absoluteFormatValue);
        }
        return vp;
    }

    @Override
    public String toString() {

        try {
            ValidityPeriod vp = this.getValidityPeriod();
            return "TPValidityPeriod [" + vp + "]";
        } catch (CAPException e) {
            return super.toString();
        }
    }

}
