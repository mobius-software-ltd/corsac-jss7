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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CategoryImpl extends ASNSingleByte implements Category {
	public CategoryImpl() { 
		super("Category",0,15,false);
    }

    public CategoryImpl(int data) {
    	super(data,"Category",0,15,false);
    }

    public CategoryImpl(CategoryValue value) {
    	super(value != null ? value.getCode() : 0,"Category",0,15,false);
    }

    public int getData() {
        return getValue();
    }

    public CategoryValue getCategoryValue() {
        return CategoryValue.getInstance(getData());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category");
        sb.append(" [");

        sb.append("Value=");
        CategoryValue val = this.getCategoryValue();
        if (val != null)
            sb.append(val);
        else
            sb.append(getData());

        sb.append("]");

        return sb.toString();
    }
}