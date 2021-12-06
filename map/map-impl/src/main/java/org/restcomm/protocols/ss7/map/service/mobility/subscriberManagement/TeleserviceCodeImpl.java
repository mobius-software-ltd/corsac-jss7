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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
public class TeleserviceCodeImpl extends ASNSingleByte implements TeleserviceCode {
	public TeleserviceCodeImpl() {       
    }

    public TeleserviceCodeImpl(int data) {
    	setValue(data);
    }

    public TeleserviceCodeImpl(TeleserviceCodeValue value) {
        setValue(value != null ? value.getCode() : 0);
    }

    public int getData() {
        return getValue();
    }

    public TeleserviceCodeValue getTeleserviceCodeValue() {
        return TeleserviceCodeValue.getInstance(this.getData());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TeleserviceCode");
        sb.append(" [");

        sb.append("Value=");
        sb.append(this.getTeleserviceCodeValue());

        sb.append(", Data=");
        sb.append(this.getData());

        sb.append("]");

        return sb.toString();
    }
}