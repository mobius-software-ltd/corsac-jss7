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
package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.map.api.service.supplementary.ASNSingleByte;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CallDiversionTreatmentIndicatorImpl extends ASNSingleByte {
	public CallDiversionTreatmentIndicatorImpl() {
        super();
    }

    public CallDiversionTreatmentIndicatorImpl(int data) {
        setValue(data);
    }

    public CallDiversionTreatmentIndicatorImpl(CallDiversionTreatmentIndicatorValue value) {
        setValue(value != null ? value.getCode() : 0);
    }

    public int getData() {
        return getValue();
    }

    public CallDiversionTreatmentIndicatorValue getCallDiversionTreatmentIndicatorValue() {
        return CallDiversionTreatmentIndicatorValue.getInstance(this.getValue());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallDiversionTreatmentIndicator");
        sb.append(" [");

        sb.append("Value=");
        sb.append(this.getCallDiversionTreatmentIndicatorValue());

        sb.append(", Data=");
        sb.append(this.getData());

        sb.append("]");

        return sb.toString();
    }

}
