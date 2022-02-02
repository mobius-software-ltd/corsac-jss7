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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallPartyConnectionResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegInformation;

/**
 *
 * @author yulian.oifa
 *
 */
public class ReleaseCallPartyConnectionParameterlessResponseImpl extends CircuitSwitchedCallMessageImpl implements
ReleaseCallPartyConnectionResponse {
	private static final long serialVersionUID = 1L;


    public ReleaseCallPartyConnectionParameterlessResponseImpl() {
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.releaseCallPartyConnection_Response;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.releaseCallPartyConnection;
    }

    @Override
    public List<LegInformation> getLegInformation() {
    	return null;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        sb.append("ReleaseCallPartyConnectionResponseIndication [");
        this.addInvokeIdInfo(sb);
        sb.append("]");

        return sb.toString();
    }
}