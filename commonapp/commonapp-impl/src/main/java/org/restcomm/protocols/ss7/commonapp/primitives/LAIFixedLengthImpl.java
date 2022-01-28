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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.APPException;
import org.restcomm.protocols.ss7.commonapp.api.APPParsingComponentException;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=false,lengthIndefinite=false)
public class LAIFixedLengthImpl extends ASNOctetString2 implements LAIFixedLength {
	
	public LAIFixedLengthImpl() {
    }

    public LAIFixedLengthImpl(int mcc, int mnc, int lac) throws APPException {
        super(translate(mcc, mnc, lac));
    }

    private static ByteBuf translate(int mcc, int mnc, int lac) throws APPException {
        if (mcc < 1 || mcc > 999)
            throw new APPException("Bad mcc value");
        if (mnc < 0 || mnc > 999)
            throw new APPException("Bad mnc value");

        ByteBuf data=Unpooled.buffer(5);        

        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (mcc < 100)
            sb.append("0");
        if (mcc < 10)
            sb.append("0");
        sb.append(mcc);

        if (mnc < 100) {
            if (mnc < 10)
                sb2.append("0");
            sb2.append(mnc);
        } else {
            sb.append(mnc % 10);
            sb2.append(mnc / 10);
        }

        TbcdStringImpl.encodeString(data, sb.toString());
        TbcdStringImpl.encodeString(data, sb2.toString());
        data.writeShort(lac);
        return data;
    }

    public int getMCC() throws APPException {
    	ByteBuf data=getValue();
        if (data == null)
            throw new APPException("Data must not be empty");
        if (data.readableBytes() != 5)
            throw new APPException("Data length must be equal 5");

        String res = null;
        try {
            res = TbcdStringImpl.decodeString(data.slice(0,3));
        } catch (APPParsingComponentException e) {
            throw new APPException("MAPParsingComponentException when decoding CellGlobalIdOrServiceAreaIdFixedLength: " + e.getMessage(), e);
        }

        if (res.length() < 5 || res.length() > 6)
            throw new APPException("Decoded TbcdString must be equal 5 or 6");

        String sMcc = res.substring(0, 3);

        return Integer.parseInt(sMcc);
    }

    public int getMNC() throws APPException {

    	ByteBuf data=getValue();
        if (data == null)
            throw new APPException("Data must not be empty");
        if (data.readableBytes() != 5)
            throw new APPException("Data length must be equal 5");

        String res = null;
        try {
            res = TbcdStringImpl.decodeString(data.slice(0,3));
        } catch (APPParsingComponentException e) {
            throw new APPException("MAPParsingComponentException when decoding CellGlobalIdOrServiceAreaIdFixedLength: " + e.getMessage(), e);
        }

        if (res.length() < 5 || res.length() > 6)
            throw new APPException("Decoded TbcdString must be equal 5 or 6");

        String sMnc;
        if (res.length() == 5) {
            sMnc = res.substring(3);
        } else {
            sMnc = res.substring(4) + res.substring(3, 4);
        }

        return Integer.parseInt(sMnc);
    }

    public int getLac() throws APPException {
    	ByteBuf data=getValue();
        if (data == null)
            throw new APPException("Data must not be empty");
        if (data.readableBytes() != 5)
            throw new APPException("Data length must be equal 5");

        data.skipBytes(3);
        int res = data.readUnsignedShort();
        return res;
    }

    @Override
    public String toString() {

        int mcc = 0;
        int mnc = 0;
        int lac = 0;
        boolean goodData = false;

        try {
            mcc = this.getMCC();
            mnc = this.getMNC();
            lac = this.getLac();
            goodData = true;
        } catch (APPException e) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LAIFixedLength");        
        sb.append(" [");
        if (goodData) {
            sb.append("MCC=");
            sb.append(mcc);
            sb.append(", MNC=");
            sb.append(mnc);
            sb.append(", Lac=");
            sb.append(lac);
        }
        sb.append("]");

        return sb.toString();
    }
}
