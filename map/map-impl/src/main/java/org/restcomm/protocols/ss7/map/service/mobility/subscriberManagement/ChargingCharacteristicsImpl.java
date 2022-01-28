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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ChargingCharacteristicsImpl extends ASNOctetString2 implements ChargingCharacteristics {
	public static final int _FLAG_NORMAL_CHARGING = 0x08;
    public static final int _FLAG_PREPAID_CHARGING = 0x04;
    public static final int _FLAG_FLAT_RATE_CHARGING_CHARGING = 0x02;
    public static final int _FLAG_CHARGING_BY_HOT_BILLING_CHARGING = 0x01;

    public ChargingCharacteristicsImpl() {        
    }

    public ChargingCharacteristicsImpl(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging) {
        super(translate(isNormalCharging, isPrepaidCharging, isFlatRateChargingCharging,
                isChargingByHotBillingCharging));
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
