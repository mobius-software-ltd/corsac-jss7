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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class ApplyChargingReportRequestImpl extends CircuitSwitchedCallMessageImpl implements ApplyChargingReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,index = -1)
    private ASNOctetString callResult;

    public ApplyChargingReportRequestImpl() {
    }

    public ApplyChargingReportRequestImpl(ByteBuf callResult) {
        if(callResult!=null)
        	this.callResult=new ASNOctetString(callResult,"ApplyChargingReportRequest",null,null,true);       
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
    public ByteBuf getCallResult() {
    	if(callResult==null)
    		return null;
    	
    	return callResult.getValue();
    }

	@Override
	public CallResultCS1 getCallResultCS1() {
		return null;
	}

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ApplyChargingReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.callResult != null || this.callResult.getValue()!=null) {
            sb.append(", callResult=");
            sb.append(callResult.printDataArr());
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
