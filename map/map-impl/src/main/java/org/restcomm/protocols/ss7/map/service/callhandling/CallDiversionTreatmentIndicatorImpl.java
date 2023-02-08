/*
 * Mobius Software LTD
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
package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicatorValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class CallDiversionTreatmentIndicatorImpl extends ASNSingleByte implements CallDiversionTreatmentIndicator {
	public CallDiversionTreatmentIndicatorImpl() {
        super("CallDiversionTreatmentIndicator",1,2,false);
    }

    public CallDiversionTreatmentIndicatorImpl(int data) {
    	super(data,"CallDiversionTreatmentIndicator",1,2,false);
    }

    public CallDiversionTreatmentIndicatorImpl(CallDiversionTreatmentIndicatorValue value) {
    	super(value != null ? value.getCode() : 0,"CallDiversionTreatmentIndicator",1,2,false);
    }

    public Integer getData() {
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
