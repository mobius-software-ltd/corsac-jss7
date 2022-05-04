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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ChargingCharacteristicsImpl extends ASNOctetString implements ChargingCharacteristics {
	public static final int _FLAG_NORMAL_CHARGING = 0x08;
    public static final int _FLAG_PREPAID_CHARGING = 0x04;
    public static final int _FLAG_FLAT_RATE_CHARGING_CHARGING = 0x02;
    public static final int _FLAG_CHARGING_BY_HOT_BILLING_CHARGING = 0x01;

    public ChargingCharacteristicsImpl() {   
    	super("ChargingCharacteristics",2,2,false);
    }

    public ChargingCharacteristicsImpl(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging) {
        super(translate(isNormalCharging, isPrepaidCharging, isFlatRateChargingCharging,
                isChargingByHotBillingCharging),"ChargingCharacteristics",2,2,false);
    }

    private static ByteBuf translate(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging){
        ByteBuf buffer=Unpooled.buffer(2);
        
        byte curr=0;
        if (isNormalCharging)
            curr |= _FLAG_NORMAL_CHARGING;
        if (isPrepaidCharging)
        	curr |= _FLAG_PREPAID_CHARGING;
        if (isFlatRateChargingCharging)
        	curr |= _FLAG_FLAT_RATE_CHARGING_CHARGING;
        if (isChargingByHotBillingCharging)
        	curr |= _FLAG_CHARGING_BY_HOT_BILLING_CHARGING;
        
        buffer.writeByte(curr);
        buffer.writeByte(0);
        return buffer;
    }

    private boolean isDataGoodFormed() {
    	ByteBuf buffer=getValue();
        if (buffer != null && buffer.readableBytes() == 2)
            return true;
        else
            return false;
    }

    public boolean isNormalCharging() {
    	ByteBuf buffer=getValue();
        if (isDataGoodFormed() && (buffer.readByte() & _FLAG_NORMAL_CHARGING) != 0)
            return true;
        else
            return false;
    }

    public boolean isPrepaidCharging() {
    	ByteBuf buffer=getValue();
        if (isDataGoodFormed() && (buffer.readByte() & _FLAG_PREPAID_CHARGING) != 0)
            return true;
        else
            return false;
    }

    public boolean isFlatRateChargingCharging() {
    	ByteBuf buffer=getValue();
        if (isDataGoodFormed() && (buffer.readByte() & _FLAG_FLAT_RATE_CHARGING_CHARGING) != 0)
            return true;
        else
            return false;
    }

    public boolean isChargingByHotBillingCharging() {
    	ByteBuf buffer=getValue();
        if (isDataGoodFormed() && (buffer.readByte() & _FLAG_CHARGING_BY_HOT_BILLING_CHARGING) != 0)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        if (isDataGoodFormed()) {
            boolean normalCharging = isNormalCharging();
            boolean prepaidCharging = isPrepaidCharging();
            boolean flatRateChargingCharging = isFlatRateChargingCharging();
            boolean chargingByHotBillingCharging = isChargingByHotBillingCharging();

            StringBuilder sb = new StringBuilder();
            sb.append("ChargingCharacteristics [Data= ");
            
            if (normalCharging) {
                sb.append(", normalCharging");
            }
            if (prepaidCharging) {
                sb.append(", prepaidCharging");
            }
            if (flatRateChargingCharging) {
                sb.append(", flatRateChargingCharging");
            }
            if (chargingByHotBillingCharging) {
                sb.append(", chargingByHotBillingCharging");
            }

            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}
