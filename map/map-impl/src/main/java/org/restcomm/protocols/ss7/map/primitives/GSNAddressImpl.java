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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GSNAddressImpl extends ASNOctetString implements GSNAddress {
	public GSNAddressImpl() {        
    }

    public GSNAddressImpl(byte[] data) {
    	setValue(Unpooled.wrappedBuffer(data));
    }

    public GSNAddressImpl(GSNAddressAddressType addressType, byte[] addressData) throws MAPException {
    	if (addressType == null)
            throw new MAPException("addressType argument must not be null");
        if (addressData == null)
            throw new MAPException("addressData argument must not be null");

        fillData(addressType, addressData);
    }

    private void fillData(GSNAddressAddressType addressType, byte[] addressData) throws MAPException {
        switch (addressType) {
        case IPv4:
            if (addressData.length != 4)
                throw new MAPException("addressData argument must have length=4 for IPv4");
            break;
        case IPv6:
            if (addressData.length != 16)
                throw new MAPException("addressData argument must have length=4 for IPv6");
            break;
        }

        byte[] data = new byte[addressData.length + 1];
        data[0] = (byte) addressType.createGSNAddressFirstByte();
        System.arraycopy(addressData, 0, data, 1, addressData.length);
        setValue(Unpooled.wrappedBuffer(data));        
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public GSNAddressAddressType getGSNAddressAddressType() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() == 0)
            return null;
        int val = data.readByte() & 0xFF;
        return GSNAddressAddressType.getFromGSNAddressFirstByte(val);
    }

    public byte[] getGSNAddressData() {
        GSNAddressAddressType type = getGSNAddressAddressType();
        if (type == null)
            return null;

        byte[] data=getData();
        switch (type) {
        case IPv4:
            if (data.length >= 5) {
                byte[] res = new byte[4];
                System.arraycopy(data, 1, res, 0, 4);
                return res;
            }
            break;
        case IPv6:
            if (data.length >= 17) {
                byte[] res = new byte[16];
                System.arraycopy(data, 1, res, 0, 16);
                return res;
            }
            break;
        }

        return null;
    }

    @Override
    public String toString() {
        GSNAddressAddressType type = getGSNAddressAddressType();
        byte[] val = getGSNAddressData();

        if (type != null && val != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("GSN Address Impl");
            sb.append(" [");

            sb.append("type=");
            sb.append(type);
            sb.append(", data=[");
            sb.append(printDataArr(val));
            sb.append("]");

            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}
