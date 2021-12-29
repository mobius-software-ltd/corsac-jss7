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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportCS1PlusRequest;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.SpecializedResourceReportChoisempl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

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
        if (this.specializedResourceReportChoisempl != null)
            return this.specializedResourceReportChoisempl.toString();
        
        return "";
    }
}
