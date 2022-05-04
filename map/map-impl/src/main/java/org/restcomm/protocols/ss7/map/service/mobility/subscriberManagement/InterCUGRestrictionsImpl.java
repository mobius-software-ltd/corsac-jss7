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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class InterCUGRestrictionsImpl extends ASNSingleByte implements InterCUGRestrictions {
	public InterCUGRestrictionsImpl() { 
		super("InterCUGRestrictions",0,3,false);
    }

    public InterCUGRestrictionsImpl(int data) {
    	super(data,"InterCUGRestrictions",0,3,false);
    }

    public InterCUGRestrictionsImpl(InterCUGRestrictionsValue val) {
    	super((val != null ? val.getCode() : 0),"InterCUGRestrictions",0,3,false);
    }

    public int getData() {
        return this.getValue();
    }

    public InterCUGRestrictionsValue getInterCUGRestrictionsValue() {
        return InterCUGRestrictionsValue.getInstance(getData());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("InterCUGRestrictions");
        sb.append(" [");

        sb.append("InterCUGRestrictions=" + this.getInterCUGRestrictionsValue());

        sb.append("]");

        return sb.toString();
    }

}
