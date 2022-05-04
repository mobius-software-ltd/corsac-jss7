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

package org.restcomm.protocols.ss7.mtp;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class Mtp3TransferPrimitiveFactory {

    private final RoutingLabelFormat pointCodeFormat;

    public Mtp3TransferPrimitiveFactory(RoutingLabelFormat pointCodeFormat) {
        this.pointCodeFormat = pointCodeFormat;
    }

    public Mtp3TransferPrimitive createMtp3TransferPrimitive(int si, int ni, int mp, int opc, int dpc, int sls, ByteBuf data) {
        Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data,
                this.pointCodeFormat);
        return mtp3TransferPrimitive;
    }

    public Mtp3TransferPrimitive createMtp3TransferPrimitive(ByteBuf msg) {
        Mtp3TransferPrimitive mtp3TransferPrimitive = null;

        // sio
        int sio = msg.readByte();
        int si = sio & 0x0F;
        int ssi = (sio & 0xF0) >> 4;
        int ni = ssi >> 2;
        int mp = ssi & 0x03;

        int dpc = 0;
        int opc = 0;
        int sls = 0;
        ByteBuf data = null;

        switch (this.pointCodeFormat) {
            case ITU:
                // routing label
                byte b1 = msg.readByte();
                byte b2 = msg.readByte();
                byte b3 = msg.readByte();
                byte b4 = msg.readByte();
                dpc = ((b2 & 0x3f) << 8) | (b1 & 0xff);
                opc = ((b4 & 0x0f) << 10) | ((b3 & 0xff) << 2) | ((b2 & 0xc0) >> 6);
                sls = ((b4 & 0xf0) >> 4);

                // msu data
                data = msg.slice();
                break;
            case ANSI_Sls8Bit:
            	 b1 = msg.readByte();
                 b2 = msg.readByte();
                 b3 = msg.readByte();                 
                 dpc = ((b3 & 0xff) << 16) | ((b2 & 0xff) << 8) | (b1 & 0xff);
                 
                 b1 = msg.readByte();
                 b2 = msg.readByte();
                 b3 = msg.readByte();                 
                 opc = ((b3 & 0xff) << 16) | ((b2 & 0xff) << 8) | (b1 & 0xff);
                 sls = (msg.readByte() & 0xff);

                // msu data
                data = msg.slice();
                break;
            case ANSI_Sls5Bit:
            	 b1 = msg.readByte();
                 b2 = msg.readByte();
                 b3 = msg.readByte();                 
                 dpc = ((b3 & 0xff) << 16) | ((b2 & 0xff) << 8) | (b1 & 0xff);
                
                 b1 = msg.readByte();
                 b2 = msg.readByte();
                 b3 = msg.readByte();                 
                 opc = ((b3 & 0xff) << 16) | ((b2 & 0xff) << 8) | (b1 & 0xff);
                 sls = (msg.readByte() & 0x1f);

                // msu data
                 data = msg.slice();
                 break;


            default:
                // TODO : We don't support rest justyet
                break;
        }
        mtp3TransferPrimitive = new Mtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data, this.pointCodeFormat);
        return mtp3TransferPrimitive;
    }
}
