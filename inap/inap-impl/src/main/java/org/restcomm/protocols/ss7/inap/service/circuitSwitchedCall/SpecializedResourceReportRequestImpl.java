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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;

/**
 *
 * @author yulian.oifa
 *
 */
public class SpecializedResourceReportRequestImpl extends CircuitSwitchedCallMessageImpl implements
        SpecializedResourceReportRequest {
	private static final long serialVersionUID = 1L;

	public SpecializedResourceReportRequestImpl() {
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.specializedResourceReport_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.specializedResourceReport;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SpecializedResourceReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        sb.append("]");

        return sb.toString();
    }
}
