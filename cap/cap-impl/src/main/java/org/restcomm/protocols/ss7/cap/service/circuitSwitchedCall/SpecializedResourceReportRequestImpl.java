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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.SpecializedResourceReportArgImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author kiss.balazs@alerant.hu
 * @author yulianoifa
 *
 */
@ASNWrappedTag
public class SpecializedResourceReportRequestImpl extends CircuitSwitchedCallMessageImpl implements
        SpecializedResourceReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private SpecializedResourceReportArgImpl arg;

    public SpecializedResourceReportRequestImpl() {
    }

    public SpecializedResourceReportRequestImpl(boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) {
        this.arg = new SpecializedResourceReportArgImpl(isAllAnnouncementsComplete, isFirstAnnouncementStarted);
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.specializedResourceReport_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.specializedResourceReport;
    }

    @Override
    public boolean getAllAnnouncementsComplete() {
    	if(arg==null)
    		return false;
    	
        return arg.getAllAnnouncementsComplete();
    }

    @Override
    public boolean getFirstAnnouncementStarted() {
    	if(arg==null)
    		return false;
    	
        return arg.getFirstAnnouncementStarted();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SpecializedResourceReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.arg != null) {
            sb.append(", arg=");
            sb.append(this.arg.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(arg==null)
			throw new ASNParsingComponentException("one of child items should be set for specialized resource report request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
