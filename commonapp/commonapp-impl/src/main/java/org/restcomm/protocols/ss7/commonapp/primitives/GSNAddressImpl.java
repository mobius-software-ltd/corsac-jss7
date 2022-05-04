/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class GSNAddressImpl extends ASNOctetString implements GSNAddress {
	public GSNAddressImpl() { 
		super("GSNAddress",5,17,false);
    }

    public GSNAddressImpl(GSNAddressAddressType addressType, ByteBuf addressData) throws ASNParsingException {
    	super(translate(addressType, addressData),"GSNAddress",5,17,false);
    }

    private static ByteBuf translate(GSNAddressAddressType addressType, ByteBuf addressData) throws ASNParsingException {
    	if (addressType == null)
            throw new ASNParsingException("addressType argument must not be null");
        if (addressData == null)
            throw new ASNParsingException("addressData argument must not be null");

        switch (addressType) {
        	case IPv4:
        		if (addressData.readableBytes() != 4)
        			throw new ASNParsingException("addressData argument must have length=4 for IPv4");
        		break;
        	case IPv6:
        		if (addressData.readableBytes() != 16)
        			throw new ASNParsingException("addressData argument must have length=4 for IPv6");
        		break;
        }

        ByteBuf typeBuffer = Unpooled.buffer(1);
        typeBuffer.writeByte(addressType.createGSNAddressFirstByte());
        return Unpooled.wrappedBuffer(typeBuffer,addressData);        
    }

    public GSNAddressAddressType getGSNAddressAddressType() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() == 0)
            return null;
        
        int val = data.readByte() & 0xFF;
        return GSNAddressAddressType.getFromGSNAddressFirstByte(val);
    }

    public ByteBuf getGSNAddressData() {
        ByteBuf value=getValue();
        if(value==null || (value.readableBytes()!=5 && value.readableBytes()!=17))
        	return null;

        return value.skipBytes(1);
    }

    @Override
    public String toString() {
        GSNAddressAddressType type = getGSNAddressAddressType();
        
        if (type != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("GSN Address Impl");
            sb.append(" [");

            sb.append("type=");
            sb.append(type);
            sb.append(", data=[");
            sb.append(printDataArr());
            sb.append("]");

            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}
