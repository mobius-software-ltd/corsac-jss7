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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.AlertingCategory;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingLevel;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class AlertingPatternImpl extends OctetStringLength1Base implements AlertingPattern {
	private static final long serialVersionUID = 1L;

	public AlertingPatternImpl() {
        super("AlertingPattern");
    }

    public AlertingPatternImpl(int data) {
        super("AlertingPattern", data);
    }

    public AlertingPatternImpl(AlertingLevel alertingLevel) {
        super("AlertingPattern", alertingLevel.getLevel());
    }

    public AlertingPatternImpl(AlertingCategory alertingCategory) {
        super("AlertingPattern", alertingCategory.getCategory());
    }

    public int getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern# getAlertingLevel()
     */
    public AlertingLevel getAlertingLevel() {
        return AlertingLevel.getInstance(this.data);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern# getAlertingCategory()
     */
    public AlertingCategory getAlertingCategory() {
        return AlertingCategory.getInstance(this.data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
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
