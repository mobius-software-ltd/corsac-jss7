/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.commonapp.smstpu.ValidityPeriodImpl;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class TPValidityPeriodImpl extends ASNOctetString implements TPValidityPeriod {
	public TPValidityPeriodImpl() {
		super("TPValidityPeriod",1,7,false);
    }

    public TPValidityPeriodImpl(int relativeFormat) {
    	super(translate(relativeFormat),"TPValidityPeriod",1,7,false);
    }
    
    private static ByteBuf translate(int relativeFormat) {
    	ByteBuf value=Unpooled.buffer(1);
    	value.writeByte(relativeFormat);
    	return value;
    }

    public TPValidityPeriodImpl(AbsoluteTimeStamp absoluteFormatValue) {
    	super(translate(absoluteFormatValue),"TPValidityPeriod",1,7,false);
    }
    
    private static ByteBuf translate(AbsoluteTimeStamp absoluteFormatValue) {    	
        ByteBuf buffer=Unpooled.buffer(7);
        try {
            absoluteFormatValue.encodeData(buffer);
        } catch (ASNParsingException e) {
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
            } catch (ASNParsingException e) {
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
