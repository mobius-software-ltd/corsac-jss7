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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingCategory;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingLevel;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class AlertingPatternImpl extends ASNSingleByte implements AlertingPattern {
	public AlertingPatternImpl() {  
		super("AlertingPattern",0,8,false);
    }

    public AlertingPatternImpl(int data) {
    	super(data,"AlertingPattern",0,8,false);
    }

    public AlertingPatternImpl(AlertingLevel alertingLevel) {
    	super(alertingLevel.getLevel(),"AlertingPattern",0,8,false);
    }

    public AlertingPatternImpl(AlertingCategory alertingCategory) {
    	super(alertingCategory.getCategory(),"AlertingPattern",0,8,false);
    }

    public Integer getData() {
    	return getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern# getAlertingLevel()
     */
    public AlertingLevel getAlertingLevel() {
        return AlertingLevel.getInstance(getData());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern# getAlertingCategory()
     */
    public AlertingCategory getAlertingCategory() {
        return AlertingCategory.getInstance(getData());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AlertingPattern");
        sb.append(" [");

        AlertingLevel al = this.getAlertingLevel();
        if (al != null) {
            sb.append("AlertingLevel=");
            sb.append(al);
        }
        AlertingCategory ac = this.getAlertingCategory();
        if (ac != null) {
            sb.append(" AlertingCategory=");
            sb.append(ac);
        }
        sb.append("]");

        return sb.toString();
    }
}
