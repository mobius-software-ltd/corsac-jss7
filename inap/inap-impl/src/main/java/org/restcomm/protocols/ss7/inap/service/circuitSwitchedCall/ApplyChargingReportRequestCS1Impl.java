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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CallResultCS1;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CallResultCS1Impl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class ApplyChargingReportRequestCS1Impl extends CircuitSwitchedCallMessageImpl implements ApplyChargingReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,index = -1, defaultImplementation = CallResultCS1Impl.class)
    private CallResultCS1 callResult;

    public ApplyChargingReportRequestCS1Impl() {
    }

    public ApplyChargingReportRequestCS1Impl(CallResultCS1 callResult) {
       this.callResult=callResult;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.applyChargingReport_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.applyChargingReport;
    }

    @Override
    public CallResultCS1 getCallResultCS1() {
    	return callResult;
    }

    @Override
    public ByteBuf getCallResult() {
    	return null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ApplyChargingReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.callResult != null) {
            sb.append(", callResult=");
            sb.append(callResult);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(callResult==null)
			throw new ASNParsingComponentException("call result should be set for apply charging report request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}