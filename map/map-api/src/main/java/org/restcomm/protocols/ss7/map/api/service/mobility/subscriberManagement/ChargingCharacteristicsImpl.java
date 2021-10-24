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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ChargingCharacteristicsImpl extends ASNOctetString {
	public static final int _FLAG_NORMAL_CHARGING = 0x08;
    public static final int _FLAG_PREPAID_CHARGING = 0x04;
    public static final int _FLAG_FLAT_RATE_CHARGING_CHARGING = 0x02;
    public static final int _FLAG_CHARGING_BY_HOT_BILLING_CHARGING = 0x01;

    public ChargingCharacteristicsImpl() {        
    }

    public ChargingCharacteristicsImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public ChargingCharacteristicsImpl(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging) {
        this.setData(isNormalCharging, isPrepaidCharging, isFlatRateChargingCharging,
                isChargingByHotBillingCharging);
    }

    protected void setData(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging){
        byte[] data = new byte[2];

        if (isNormalCharging)
            data[0] |= _FLAG_NORMAL_CHARGING;
        if (isPrepaidCharging)
            data[0] |= _FLAG_PREPAID_CHARGING;
        if (isFlatRateChargingCharging)
            data[0] |= _FLAG_FLAT_RATE_CHARGING_CHARGING;
        if (isChargingByHotBillingCharging)
            data[0] |= _FLAG_CHARGING_BY_HOT_BILLING_CHARGING;
        
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

    private boolean isDataGoodFormed() {
    	byte[] data=getData();
        if (data != null && data.length == 2)
            return true;
        else
            return false;
    }

    public boolean isNormalCharging() {
    	byte[] data=getData();
    	if (isDataGoodFormed() && (data[0] & _FLAG_NORMAL_CHARGING) != 0)
            return true;
        else
            return false;
    }

    public boolean isPrepaidCharging() {
    	byte[] data=getData();
    	if (isDataGoodFormed() && (data[0] & _FLAG_PREPAID_CHARGING) != 0)
            return true;
        else
            return false;
    }

    public boolean isFlatRateChargingCharging() {
    	byte[] data=getData();
    	if (isDataGoodFormed() && (data[0] & _FLAG_FLAT_RATE_CHARGING_CHARGING) != 0)
            return true;
        else
            return false;
    }

    public boolean isChargingByHotBillingCharging() {
    	byte[] data=getData();
    	if (isDataGoodFormed() && (data[0] & _FLAG_CHARGING_BY_HOT_BILLING_CHARGING) != 0)
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
            sb.append(printDataArr(getData()));

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
