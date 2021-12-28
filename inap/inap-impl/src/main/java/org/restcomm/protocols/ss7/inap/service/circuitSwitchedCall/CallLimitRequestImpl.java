/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapTreatmentImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.DateAndTimeImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallLimitRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LimitIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.LimitIndicatorsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CallLimitRequestImpl extends CircuitSwitchedCallMessageImpl implements CallLimitRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = DateAndTimeImpl.class)
    private DateAndTime startTime;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1,defaultImplementation = GapCriteriaImpl.class)
    private GapCriteria limitCriteria;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = LimitIndicatorsImpl.class)
    private LimitIndicators limitIndicators;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = GapTreatmentImpl.class)
    private GapTreatment limitTreatment;
    
    public CallLimitRequestImpl() {
    }

    public CallLimitRequestImpl(DateAndTime startTime,GapCriteria limitCriteria,LimitIndicators limitIndicators,GapTreatment limitTreatment) {    	
    	this.startTime=startTime;                   	
    	this.limitCriteria=limitCriteria;
    	this.limitIndicators=limitIndicators;
    	this.limitTreatment=limitTreatment;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.callLimit_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.callLimit;
    }

    @Override
    public DateAndTime getStartTime() {
		return startTime;
	}

    @Override
    public GapCriteria getLimitCriteria() {
		return limitCriteria;
	}

    @Override
    public LimitIndicators getLimitIndicators() {
		return limitIndicators;
	}

    @Override
    public GapTreatment getLimitTreatment() {
		return limitTreatment;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CallLimitRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.startTime != null) {
            sb.append(", startTime=");
            sb.append(startTime);
        }
        if (this.limitCriteria != null) {
            sb.append(", limitCriteria=");
            sb.append(limitCriteria.toString());
        }
        if (this.limitIndicators != null) {
            sb.append(", limitIndicators=");
            sb.append(limitIndicators);
        }
        if (this.limitTreatment != null) {
            sb.append(", limitTreatment=");
            sb.append(limitTreatment.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
