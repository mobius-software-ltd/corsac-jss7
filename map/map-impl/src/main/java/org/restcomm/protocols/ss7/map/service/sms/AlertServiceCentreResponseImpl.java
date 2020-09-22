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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;

public class AlertServiceCentreResponseImpl extends SmsMessageImpl implements AlertServiceCentreResponse {
	private static final long serialVersionUID = 1L;

	public MAPMessageType getMessageType() {
        return MAPMessageType.alertServiceCentre_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.alertServiceCentre;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AlertServiceCentreResponse [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        sb.append("]");

        return sb.toString();
    }
}
