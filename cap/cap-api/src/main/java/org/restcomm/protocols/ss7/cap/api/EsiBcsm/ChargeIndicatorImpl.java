/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.EsiBcsm;

import org.restcomm.protocols.ss7.map.api.service.supplementary.ASNSingleByte;

/**
*
* @author sergey vetyutnev
*
*/
public class ChargeIndicatorImpl extends ASNSingleByte {
	public ChargeIndicatorImpl() {
		
	}
	
	public ChargeIndicatorImpl(int data) {
		setValue(data);
    }

    public ChargeIndicatorImpl(ChargeIndicatorValue value) {
        if (value != null)
            setValue(value.getCode());
    }

    public int getData() {
        return getValue();
    }

    public ChargeIndicatorValue getChargeIndicatorValue() {
        return ChargeIndicatorValue.getInstance(getValue());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargeIndicator [");

        ChargeIndicatorValue value = this.getChargeIndicatorValue();
        if (value != null) {
            sb.append("chargeIndicatorValue=");
            sb.append(value);
        }

        sb.append("]");

        return sb.toString();
    }

}
