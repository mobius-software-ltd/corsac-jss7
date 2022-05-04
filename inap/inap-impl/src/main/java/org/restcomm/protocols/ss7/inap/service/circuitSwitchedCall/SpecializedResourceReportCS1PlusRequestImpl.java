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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportCS1PlusRequest;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.SpecializedResourceReportChoisempl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class SpecializedResourceReportCS1PlusRequestImpl extends CircuitSwitchedCallMessageImpl implements SpecializedResourceReportCS1PlusRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private SpecializedResourceReportChoisempl specializedResourceReportChoisempl;

    public SpecializedResourceReportCS1PlusRequestImpl() {
    }

    public SpecializedResourceReportCS1PlusRequestImpl(boolean value,boolean isStarted) {
        this.specializedResourceReportChoisempl = new SpecializedResourceReportChoisempl(value,isStarted);
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
    public boolean getAnnouncementCompleted() {
    	if(specializedResourceReportChoisempl==null)
    		return false;
    	
        return specializedResourceReportChoisempl.getAnnouncementCompleted();
    }

    @Override
    public boolean getAnnouncementStarted() {
    	if(specializedResourceReportChoisempl==null)
    		return false;
    	
        return specializedResourceReportChoisempl.getAnnouncementStarted();
    }

    @Override
    public String toString() {
    	 StringBuilder sb = new StringBuilder();
         if (this.specializedResourceReportChoisempl != null)
         	sb.append(this.specializedResourceReportChoisempl.toString());
         
         this.addInvokeIdInfo(sb);        
         return sb.toString();        
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(specializedResourceReportChoisempl==null)
			throw new ASNParsingComponentException("one of child items should be set for specialized resource report request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
