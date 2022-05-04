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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeDurationChargingResult;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeDurationChargingResultImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class ApplyChargingReportRequestImpl extends CircuitSwitchedCallMessageImpl implements ApplyChargingReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = TimeDurationChargingResultImpl.class)
    private TimeDurationChargingResult timeDurationChargingResult;

    public ApplyChargingReportRequestImpl() {
    }

    public ApplyChargingReportRequestImpl(TimeDurationChargingResult timeDurationChargingResult) {
        this.timeDurationChargingResult = timeDurationChargingResult;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.applyChargingReport_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.applyChargingReport;
    }

    @Override
    public TimeDurationChargingResult getTimeDurationChargingResult() {
        return timeDurationChargingResult;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ApplyChargingReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.timeDurationChargingResult != null) {
            sb.append(", timeDurationChargingResult=");
            sb.append(timeDurationChargingResult.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timeDurationChargingResult==null)
			throw new ASNParsingComponentException("time duration charging result should be set for apply charging report request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
