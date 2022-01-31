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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.commonapp.primitives.TbcdStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AreaIdentificationImpl extends ASNOctetString implements AreaIdentification {
	
	public AreaIdentificationImpl() {
    }

    public AreaIdentificationImpl(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId) throws MAPException {
    	super(translate(type, mcc, mnc, lac, Rac_CellId_UtranCellId));
    }
    
    private static ByteBuf translate(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId) throws MAPException {
        if (type == null)
            throw new MAPException("type is undefined");
        if (mcc < 1 || mcc > 999)
            throw new MAPException("Bad mcc value");
        if (mnc < 0 || mnc > 999)
            throw new MAPException("Bad mnc value");

        ByteBuf result;
        switch (type) {
            case countryCode:
            	result = Unpooled.buffer(2);
                break;
            case plmnId:
            	result = Unpooled.buffer(3);
                break;
            case locationAreaId:
            	result = Unpooled.buffer(5);
                break;
            case routingAreaId:
            	result = Unpooled.buffer(6);
                break;
            case cellGlobalId:
            case utranCellId:
            	result = Unpooled.buffer(7);
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

        result.resetWriterIndex();
        try {
	        TbcdStringImpl.encodeString(result, sb.toString());
	        
	        if (type != AreaType.countryCode) {
	        	TbcdStringImpl.encodeString(result, sb2.toString());            
	        }
        }
        catch(ASNParsingException ex) {
        	throw new MAPException(ex.getMessage(), ex.getCause());
        }

        if (type == AreaType.locationAreaId || type == AreaType.routingAreaId || type == AreaType.cellGlobalId) {
            result.writeByte(lac / 256);
            result.writeByte(lac % 256);
        }

        if (type == AreaType.routingAreaId) {
        	result.writeByte(Rac_CellId_UtranCellId);
        }

        if (type == AreaType.cellGlobalId) {
        	result.writeByte(Rac_CellId_UtranCellId / 256);
        	result.writeByte(Rac_CellId_UtranCellId % 256);
        }

        if (type == AreaType.utranCellId) {
        	result.writeByte((Rac_CellId_UtranCellId >> 24) & 0xFF);
        	result.writeByte((Rac_CellId_UtranCellId >> 16) & 0xFF);
        	result.writeByte((Rac_CellId_UtranCellId >> 8) & 0xFF);
        	result.writeByte((Rac_CellId_UtranCellId) & 0xFF);
        }
        
        return result;
    }

    public int getMCC() throws MAPException {
    	ByteBuf buffer=getValue();    			
        if (buffer == null)
            throw new MAPException("Data must not be empty");
        if (buffer.readableBytes() < 2)
            throw new MAPException("Data length must be at least 2");
        
        String res;
        try {
            res = TbcdStringImpl.decodeString(buffer.readSlice(2));
        } catch (ASNParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when decoding TbcdString: " + e.getMessage(), e);
        }

        if (res.length() < 3 || res.length() > 4)
            throw new MAPException("Decoded TbcdString must be equal 3 or 4");

        String sMcc = res.substring(0, 3);

        return Integer.parseInt(sMcc);
    }

    public int getMNC() throws MAPException {
    	ByteBuf buffer=getValue();    			
        if (buffer == null)
            throw new MAPException("Data must not be empty");
        if (buffer.readableBytes() < 3)
            throw new MAPException("Data length must be at least 3");

        String res = null;
        try {        	
            res = TbcdStringImpl.decodeString(buffer.readSlice(3));
        } catch (ASNParsingComponentException e) {
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
    	ByteBuf buffer=getValue();    			
        if (buffer == null)
            throw new MAPException("Data must not be empty");
        if (buffer.readableBytes() < 5)
            throw new MAPException("Data length must be at least 5");

        buffer.skipBytes(3);
        int res = (buffer.readByte() & 0x0FF) * 256 + (buffer.readByte() & 0x0FF);
        return res;
    }

    public int getRac() throws MAPException {
    	ByteBuf buffer=getValue();    			
        if (buffer == null)
            throw new MAPException("Data must not be empty");
        if (buffer.readableBytes() < 6)
            throw new MAPException("Data length must be at least 6");

        buffer.skipBytes(5);
        int res = (buffer.readByte() & 0x0FF);
        return res;
    }

    public int getCellId() throws MAPException {
    	ByteBuf buffer=getValue();    			
        if (buffer == null)
            throw new MAPException("Data must not be empty");
        if (buffer.readableBytes() < 7)
            throw new MAPException("Data length must be at least 7");

        buffer.skipBytes(5);
        int res = (buffer.readByte() & 0x0FF) * 256 + (buffer.readByte() & 0x0FF);
        return res;
    }

    public int getUtranCellId() throws MAPException {
    	ByteBuf buffer=getValue();    			
        if (buffer == null)
            throw new MAPException("Data must not be empty");
        if (buffer.readableBytes() < 7)
            throw new MAPException("Data length must be at least 7");

        buffer.skipBytes(3);
        int res = ((buffer.readByte() & 0x0FF) << 24) + ((buffer.readByte() & 0x0FF) << 16) + ((buffer.readByte() & 0x0FF) << 8) + (buffer.readByte() & 0x0FF);
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
