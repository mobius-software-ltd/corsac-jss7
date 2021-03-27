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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.primitives.TbcdString;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AreaIdentificationImpl extends ASNOctetString {
	
	public AreaIdentificationImpl() {
    }

    public AreaIdentificationImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public AreaIdentificationImpl(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId) throws MAPException {
        if (type == null)
            throw new MAPException("type is undefined");
        if (mcc < 1 || mcc > 999)
            throw new MAPException("Bad mcc value");
        if (mnc < 0 || mnc > 999)
            throw new MAPException("Bad mnc value");

        byte[] data;
        switch (type) {
            case countryCode:
                data = new byte[2];
                break;
            case plmnId:
                data = new byte[3];
                break;
            case locationAreaId:
                data = new byte[5];
                break;
            case routingAreaId:
                data = new byte[6];
                break;
            case cellGlobalId:
            case utranCellId:
                data = new byte[7];
                break;
            default:
                throw new MAPException("Bad type value");
        }

        StringBuilder sb = new StringBuilder();
        if (mcc < 100)
            sb.append("0");
        if (mcc < 10)
            sb.append("0");
        sb.append(mcc);

        StringBuilder sb2 = new StringBuilder();
        if (type != AreaType.countryCode) {
            if (mnc < 100) {
                if (mnc < 10)
                    sb2.append("0");
                sb2.append(mnc);
            } else {
                sb.append(mnc % 10);
                sb2.append(mnc / 10);
            }
        }

        ByteBuf result=Unpooled.wrappedBuffer(data);
        result.resetWriterIndex();
        TbcdString.encodeString(result, sb.toString());
        
        if (type != AreaType.countryCode) {
        	TbcdString.encodeString(result, sb2.toString());            
        }

        if (type == AreaType.locationAreaId || type == AreaType.routingAreaId || type == AreaType.cellGlobalId) {
            data[3] = (byte) (lac / 256);
            data[4] = (byte) (lac % 256);
        }

        if (type == AreaType.routingAreaId) {
            data[5] = (byte) (Rac_CellId_UtranCellId);
        }

        if (type == AreaType.cellGlobalId) {
            data[5] = (byte) (Rac_CellId_UtranCellId / 256);
            data[6] = (byte) (Rac_CellId_UtranCellId % 256);
        }

        if (type == AreaType.utranCellId) {
            data[3] = (byte) ((Rac_CellId_UtranCellId >> 24) & 0xFF);
            data[4] = (byte) ((Rac_CellId_UtranCellId >> 16) & 0xFF);
            data[5] = (byte) ((Rac_CellId_UtranCellId >> 8) & 0xFF);
            data[6] = (byte) ((Rac_CellId_UtranCellId) & 0xFF);
        }
        
        setValue(Unpooled.wrappedBuffer(data));
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public int getMCC() throws MAPException {
    	byte[] data=getData();    			
        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 2)
            throw new MAPException("Data length must be at least 2");
        
        String res;
        try {
            res = TbcdString.decodeString(Unpooled.wrappedBuffer(data,0,2));
        } catch (MAPParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when decoding TbcdString: " + e.getMessage(), e);
        }

        if (res.length() < 3 || res.length() > 4)
            throw new MAPException("Decoded TbcdString must be equal 3 or 4");

        String sMcc = res.substring(0, 3);

        return Integer.parseInt(sMcc);
    }

    public int getMNC() throws MAPException {
    	byte[] data=getData();    			
        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 3)
            throw new MAPException("Data length must be at least 3");

        String res = null;
        try {
            res = TbcdString.decodeString(Unpooled.wrappedBuffer(data,0,3));
        } catch (MAPParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when decoding TbcdString: " + e.getMessage(), e);
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
    	byte[] data=getData();    			
        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 5)
            throw new MAPException("Data length must be at least 5");

        int res = (data[3] & 0xFF) * 256 + (data[4] & 0xFF);
        return res;
    }

    public int getRac() throws MAPException {
    	byte[] data=getData();    			
        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 6)
            throw new MAPException("Data length must be at least 6");

        int res = (data[5] & 0xFF);
        return res;
    }

    public int getCellId() throws MAPException {
    	byte[] data=getData();    			
        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 7)
            throw new MAPException("Data length must be at least 7");

        int res = (data[5] & 0xFF) * 256 + (data[6] & 0xFF);
        return res;
    }

    public int getUtranCellId() throws MAPException {
    	byte[] data=getData();    			
        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 7)
            throw new MAPException("Data length must be at least 7");

        int res = ((data[3] & 0xFF) << 24) + ((data[4] & 0xFF) << 16) + ((data[5] & 0xFF) << 8) + (data[6] & 0xFF);
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AreaIdentification [");

        try {
            int mcc = this.getMCC();
            sb.append("mcc=");
            sb.append(mcc);
        } catch (MAPException e) {
        }
        try {
            int mnc = this.getMNC();
            sb.append(", mnc=");
            sb.append(mnc);
        } catch (MAPException e) {
        }
        try {
            int lac = this.getLac();
            sb.append(", lac=");
            sb.append(lac);
        } catch (MAPException e) {
        }
        try {
            int rac = this.getRac();
            sb.append(", rac=");
            sb.append(rac);
        } catch (MAPException e) {
        }
        try {
            int cellId = this.getCellId();
            sb.append(", cellId=");
            sb.append(cellId);
        } catch (MAPException e) {
        }
        try {
            int utranCellId = this.getUtranCellId();
            sb.append(", utranCellId=");
            sb.append(utranCellId);
        } catch (MAPException e) {
        }

        sb.append("]");

        return sb.toString();
    }
}
