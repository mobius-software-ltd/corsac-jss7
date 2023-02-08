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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.commonapp.primitives.TbcdStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;

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
public class GlobalCellIdImpl extends ASNOctetString implements GlobalCellId {
	
	public GlobalCellIdImpl() {
        super("GlobalCellId",5,7,false);
    }

    public GlobalCellIdImpl(int mcc, int mnc, int lac, int cellId)
            throws MAPException {
        super(translate(mcc, mnc, lac, cellId),"GlobalCellId",5,7,false);
    }

    public static ByteBuf translate(int mcc, int mnc, int lac, int cellId) throws MAPException {
        if (mcc < 1 || mcc > 999)
            throw new MAPException("Bad mcc value");
        if (mnc < 0 || mnc > 999)
            throw new MAPException("Bad mnc value");

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

        ByteBuf result=Unpooled.buffer(7);
        try {
        	TbcdStringImpl.encodeString(result, sb.toString());
        	TbcdStringImpl.encodeString(result, sb2.toString());
        }
        catch(ASNParsingException ex) {
        	throw new MAPException(ex.getMessage(), ex.getCause());
        }
        
        result.writeByte((byte) (lac / 256));
        result.writeByte((byte) (lac % 256));
        result.writeByte((byte) (cellId / 256));
        result.writeByte((byte) (cellId % 256));
        
        return result;
    }

    public int getMcc() throws MAPException {
    	ByteBuf value=getValue();
        if (value == null)
            throw new MAPException("Data must not be empty");
        if (value.readableBytes() < 5 || value.readableBytes() > 7)
            throw new MAPException("Data length must be between 5-7");

        String res = null;
        try {
            res = TbcdStringImpl.decodeString(value.readSlice(3));
        }catch (ASNParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when decoding GlobalCellId: " + e.getMessage(), e);
        }

        if (res.length() < 5 || res.length() > 6)
            throw new MAPException("Decoded TbcdString must be equal 5 or 6");

        String sMcc = res.substring(0, 3);

        return Integer.parseInt(sMcc);
    }

    public int getMnc() throws MAPException {
    	ByteBuf value=getValue();
        if (value == null)
            throw new MAPException("Data must not be empty");
        if (value.readableBytes() < 5 || value.readableBytes() > 7)
            throw new MAPException("Data length must be between 5-7");

        String res = null;
        try {
            res = TbcdStringImpl.decodeString(value.readSlice(3));
        } catch (ASNParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when decoding GlobalCellId: " + e.getMessage(), e);
        }

        if (res.length() < 5 || res.length() > 6)
            throw new MAPException("Decoded TbcdString must be equal 5 or 6");

        String sMnc;
        if (res.length() == 5) {
            sMnc = res.substring(3);
        } else {
            sMnc = res.substring(4) + res.substring(3, 4);
        }

        return Integer.parseInt(sMnc);
    }

    public int getLac() throws MAPException {
    	ByteBuf value=getValue();
        if (value == null)
            throw new MAPException("Data must not be empty");
        if (value.readableBytes() < 5 || value.readableBytes() > 7)
            throw new MAPException("Data length must be between 5-7");

        value.skipBytes(3);
        int res = (value.readByte() & 0xFF) * 256 + (value.readByte() & 0xFF);
        return res;
    }

    public int getCellId() throws MAPException {
    	ByteBuf value=getValue();
        if (value == null)
            throw new MAPException("Data must not be empty");
        if (value.readableBytes() < 5 || value.readableBytes() > 7)
            throw new MAPException("Data length must be between 5-7");

        int res = 0;
        if (value.readableBytes() == 7) {
        	value.skipBytes(5);
            res = (value.readByte() & 0xFF) * 256 + (value.readByte() & 0xFF);
        }
        
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
            mcc = this.getMcc();
            mnc = this.getMnc();
            lac = this.getLac();
            cellId = this.getCellId();
            goodData = true;
        } catch (MAPException e) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append("GlobalCellId");
        sb.append(" [");
        if (goodData) {
            sb.append("MCC=");
            sb.append(mcc);
            sb.append(", MNC=");
            sb.append(mnc);
            sb.append(", Lac=");
            sb.append(lac);
            sb.append(", CellId=");
            sb.append(cellId);
        } else {
            sb.append("Invalid Data");            
        }
        sb.append("]");

        return sb.toString();
    }
}
