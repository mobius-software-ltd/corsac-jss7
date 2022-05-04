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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CellGlobalIdOrServiceAreaIdFixedLengthImpl extends ASNOctetString implements CellGlobalIdOrServiceAreaIdFixedLength {
	
	public CellGlobalIdOrServiceAreaIdFixedLengthImpl() {
		super("CellGlobalIdOrServiceAreaIdFixedLength",7,7,false);
    }

    public CellGlobalIdOrServiceAreaIdFixedLengthImpl(int mcc, int mnc, int lac, int cellIdOrServiceAreaCode)
            throws ASNParsingException {
        super(translate(mcc, mnc, lac, cellIdOrServiceAreaCode),"CellGlobalIdOrServiceAreaIdFixedLength",7,7,false);
    }

    public static ByteBuf translate(int mcc, int mnc, int lac, int cellIdOrServiceAreaCode) throws ASNParsingException {
        if (mcc < 1 || mcc > 999)
            throw new ASNParsingException("Bad mcc value");
        if (mnc < 0 || mnc > 999)
            throw new ASNParsingException("Bad mnc value");

        ByteBuf data=Unpooled.buffer(7);        
        
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
        data.writeShort(cellIdOrServiceAreaCode);
        return data;
    }

    public int getMCC() throws ASNParsingException {
    	ByteBuf data=getValue();
        if (data == null)
            throw new ASNParsingException("Data must not be empty");
        if (data.readableBytes() != 7)
            throw new ASNParsingException("Data length must be equal 7");

        String res = null;
        try {
            res = TbcdStringImpl.decodeString(data.slice(0, 3));
        } catch (ASNParsingComponentException e) {
            throw new ASNParsingException("APPParsingComponentException when decoding CellGlobalIdOrServiceAreaIdFixedLength: " + e.getMessage(), e);
        }

        if (res.length() < 5 || res.length() > 6)
            throw new ASNParsingException("Decoded TbcdString must be equal 5 or 6");

        String sMcc = res.substring(0, 3);

        return Integer.parseInt(sMcc);
    }

    public int getMNC() throws ASNParsingException {

    	ByteBuf data=getValue();
        if (data == null)
            throw new ASNParsingException("Data must not be empty");
        if (data.readableBytes() != 7)
            throw new ASNParsingException("Data length must be equal 7");

        String res = null;
        try {
            res = TbcdStringImpl.decodeString(data.slice(0, 3));
        } catch (ASNParsingComponentException e) {
            throw new ASNParsingException("APPParsingComponentException when decoding CellGlobalIdOrServiceAreaIdFixedLength: " + e.getMessage(), e);
        }

        if (res.length() < 5 || res.length() > 6)
            throw new ASNParsingException("Decoded TbcdString must be equal 5 or 6");

        String sMnc;
        if (res.length() == 5) {
            sMnc = res.substring(3);
        } else {
            sMnc = res.substring(4) + res.substring(3, 4);
        }

        return Integer.parseInt(sMnc);
    }

    public int getLac() throws ASNParsingException {
    	ByteBuf data=getValue();
        if (data == null)
            throw new ASNParsingException("Data must not be empty");
        if (data.readableBytes() != 7)
            throw new ASNParsingException("Data length must be equal 7");

        data.skipBytes(3);
        int res = data.readUnsignedShort();
        return res;
    }

    public int getCellIdOrServiceAreaCode() throws ASNParsingException {
    	ByteBuf data=getValue();
        if (data == null)
            throw new ASNParsingException("Data must not be empty");
        if (data.readableBytes() != 7)
            throw new ASNParsingException("Data length must be equal 7");

        data.skipBytes(5);
        int res = data.readUnsignedShort();
        return res;
    }

    @Override
    public String toString() {

        int mcc = 0;
        int mnc = 0;
        int lac = 0;
        int cellId = 0;
        boolean goodData = false;

        try {
            mcc = this.getMCC();
            mnc = this.getMNC();
            lac = this.getLac();
            cellId = this.getCellIdOrServiceAreaCode();
            goodData = true;
        } catch (ASNParsingException e) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CellGlobalIdOrServiceAreaIdFixedLength");        
        sb.append(" [");
        if (goodData) {
            sb.append("MCC=");
            sb.append(mcc);
            sb.append(", MNC=");
            sb.append(mnc);
            sb.append(", Lac=");
            sb.append(lac);
            sb.append(", CellId(SAC)=");
            sb.append(cellId);
        }
        sb.append("]");

        return sb.toString();
    }
}
