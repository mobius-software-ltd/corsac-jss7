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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InterCUGRestrictionsImpl extends ASNSingleByte implements InterCUGRestrictions {
	public InterCUGRestrictionsImpl() {        
    }

    public InterCUGRestrictionsImpl(int data) {
    	super(data);
    }

    public InterCUGRestrictionsImpl(InterCUGRestrictionsValue val) {
    	super((val != null ? val.getCode() : 0));
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
