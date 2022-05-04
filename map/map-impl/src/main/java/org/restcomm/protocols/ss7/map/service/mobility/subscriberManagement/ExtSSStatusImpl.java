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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ExtSSStatusImpl extends ASNSingleByte implements ExtSSStatus {
	/**
     * SSStatus bits TS 3GPP TS 23.011
     */
    public static final byte sssBitQ = 8; // bit 4
    public static final byte sssBitP = 4; // bit 3
    public static final byte sssBitR = 2; // bit 2
    public static final byte sssBitA = 1; // bit 1

    public ExtSSStatusImpl() {
    	super("ExtSSStatus",0,15,false);
    }

    public ExtSSStatusImpl(boolean bitQ, boolean bitP, boolean bitR, boolean bitA) {
    	super(translate(bitQ, bitP, bitR, bitA),"ExtSSStatus",0,15,false);
    }
    
    public static Integer translate(boolean bitQ, boolean bitP, boolean bitR, boolean bitA) {
        Integer data=0;

        if (bitQ)
            data |= sssBitQ;
        if (bitP)
            data |= sssBitP;
        if (bitR)
            data |= sssBitR;
        if (bitA)
            data |= sssBitA;
        
        return data;
    }

    public boolean getBitQ() {
    	Integer value=getValue();
        if (value == null)
            return false;

        return (((value & sssBitQ) > 0) ? true : false);
    }

    public boolean getBitP() {
    	Integer value=getValue();
    	if (value == null)
            return false;

        return (((value & sssBitP) > 0) ? true : false);
    }

    public boolean getBitR() {
    	Integer value=getValue();
    	if (value == null)
    		return false;

        return (((value & sssBitR) > 0) ? true : false);
    }

    public boolean getBitA() {
    	Integer value=getValue();
        if (value == null)
            return false;

        return (((value & sssBitA) > 0) ? true : false);
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
