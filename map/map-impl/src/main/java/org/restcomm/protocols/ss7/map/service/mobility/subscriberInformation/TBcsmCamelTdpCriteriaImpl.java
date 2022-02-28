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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ASNTBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CauseValueWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class TBcsmCamelTdpCriteriaImpl implements TBcsmCamelTdpCriteria {
	private ASNTBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private ExtBasicServiceCodeListWrapperImpl basicServiceCriteria;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private CauseValueWrapperImpl tCauseValueCriteria;

    public TBcsmCamelTdpCriteriaImpl() {
    }

    public TBcsmCamelTdpCriteriaImpl(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint,
            List<ExtBasicServiceCode> basicServiceCriteria, List<CauseValue> tCauseValueCriteria) {
        if(tBcsmTriggerDetectionPoint!=null)
        	this.tBcsmTriggerDetectionPoint = new ASNTBcsmTriggerDetectionPoint(tBcsmTriggerDetectionPoint);
        	
        if(basicServiceCriteria!=null)
        	this.basicServiceCriteria = new ExtBasicServiceCodeListWrapperImpl(basicServiceCriteria);
        
        if(tCauseValueCriteria!=null)
        	this.tCauseValueCriteria = new CauseValueWrapperImpl(tCauseValueCriteria);
    }

    public TBcsmTriggerDetectionPoint getTBcsmTriggerDetectionPoint() {
    	if(tBcsmTriggerDetectionPoint==null)
    		return null;
    	
        return this.tBcsmTriggerDetectionPoint.getType();
    }

    public List<ExtBasicServiceCode> getBasicServiceCriteria() {
    	if(this.basicServiceCriteria==null)
    		return null;
    	
        return this.basicServiceCriteria.getExtBasicServiceCode();
    }

    public List<CauseValue> getTCauseValueCriteria() {
    	if(this.tCauseValueCriteria==null)
    		return null;
    	
        return this.tCauseValueCriteria.getCauseValueImpl();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TBcsmCamelTdpCriteria [");

        if (this.tBcsmTriggerDetectionPoint != null) {
            sb.append("tBcsmTriggerDetectionPoint=");
            sb.append(this.tBcsmTriggerDetectionPoint.toString());
            sb.append(", ");
        }

        if (this.basicServiceCriteria != null && this.basicServiceCriteria.getExtBasicServiceCode()!=null) {
            sb.append("basicServiceCriteria=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicServiceCriteria.getExtBasicServiceCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.tCauseValueCriteria != null && this.tCauseValueCriteria.getCauseValueImpl()!=null) {
            sb.append("tCauseValueCriteria=[");
            boolean firstItem = true;
            for (CauseValue be : this.tCauseValueCriteria.getCauseValueImpl()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tBcsmTriggerDetectionPoint==null)
			throw new ASNParsingComponentException("tbcsm trigger detection point should be set for tbcsm camel tdp criteria", ASNParsingComponentExceptionReason.MistypedParameter);

		if(basicServiceCriteria!=null && basicServiceCriteria.getExtBasicServiceCode()!=null && basicServiceCriteria.getExtBasicServiceCode().size()>5)
			throw new ASNParsingComponentException("basic service criteria size should be between 1 and 5 for tbcsm camel tdp criteria", ASNParsingComponentExceptionReason.MistypedParameter);

		if(tCauseValueCriteria!=null && tCauseValueCriteria.getCauseValueImpl()!=null && tCauseValueCriteria.getCauseValueImpl().size()>5)
			throw new ASNParsingComponentException("tcause value criteria size should be between 1 and 5 for tbcsm camel tdp criteria", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
