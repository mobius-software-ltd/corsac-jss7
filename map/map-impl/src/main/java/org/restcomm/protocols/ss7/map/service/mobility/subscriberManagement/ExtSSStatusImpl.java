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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtSSStatusImpl extends ASNOctetString implements ExtSSStatus {
	/**
     * SSStatus bits TS 3GPP TS 23.011
     */
    public static final byte sssBitQ = 8; // bit 4
    public static final byte sssBitP = 4; // bit 3
    public static final byte sssBitR = 2; // bit 2
    public static final byte sssBitA = 1; // bit 1

    public ExtSSStatusImpl() {
    }

    public ExtSSStatusImpl(byte data) {
        setValue(Unpooled.wrappedBuffer(new byte[] {(byte)(data & 0x0FF)}));
    }

    public ExtSSStatusImpl(boolean bitQ, boolean bitP, boolean bitR, boolean bitA) {
        Integer data=0;

        if (bitQ)
            data |= sssBitQ;
        if (bitP)
            data |= sssBitP;
        if (bitR)
            data |= sssBitR;
        if (bitA)
            data |= sssBitA;
        
        setValue(Unpooled.wrappedBuffer(new byte[] {(byte)(data & 0x0FF)}));
    }

    public byte getData() {
    	ByteBuf buffer=getValue();
    	if(buffer==null || buffer.readableBytes()==0)
    		return 0;
    	
    	return buffer.readByte();
    }

    public boolean getBitQ() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes()==0)
            return false;

        return (((data.readByte() & sssBitQ) > 0) ? true : false);
    }

    public boolean getBitP() {
    	ByteBuf data=getValue();
    	if (data == null || data.readableBytes()==0)
            return false;

        return (((data.readByte() & sssBitP) > 0) ? true : false);
    }

    public boolean getBitR() {
    	ByteBuf data=getValue();
    	if (data == null || data.readableBytes()==0)
            return false;

        return (((data.readByte() & sssBitR) > 0) ? true : false);
    }

    public boolean getBitA() {
    	ByteBuf data=getValue();
    	if (data == null || data.readableBytes()==0)
            return false;

        return (((data.readByte() & sssBitA) > 0) ? true : false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtSSStatus [");

        if (this.getBitQ()) {
            sb.append("bitQ, ");
        }
        if (this.getBitP()) {
            sb.append("bitP, ");
        }
        if (this.getBitR()) {
            sb.append("bitR, ");
        }
        if (this.getBitA()) {
            sb.append("bitA, ");
        }

        sb.append("]");

        return sb.toString();
    }
}
