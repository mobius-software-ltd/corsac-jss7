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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MTsmsCAMELTDPCriteriaImpl implements MTsmsCAMELTDPCriteria {
	private ASNSMSTriggerDetectionPoint smsTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private MTSMSTPDUTypeWrapperImpl tPDUTypeCriterion;

    public MTsmsCAMELTDPCriteriaImpl() {
    }

    public MTsmsCAMELTDPCriteriaImpl(SMSTriggerDetectionPoint smsTriggerDetectionPoint,
            List<MTSMSTPDUType> tPDUTypeCriterion) {
        if(smsTriggerDetectionPoint!=null)
        	this.smsTriggerDetectionPoint = new ASNSMSTriggerDetectionPoint(smsTriggerDetectionPoint);
        	
        if(tPDUTypeCriterion!=null)
        {
        	List<ASNMTSMSTPDUType> wrappedData=new ArrayList<ASNMTSMSTPDUType>();
        	for(MTSMSTPDUType curr:tPDUTypeCriterion) {
        		ASNMTSMSTPDUType item=new ASNMTSMSTPDUType(curr);
        		wrappedData.add(item);
        	}
        	
        	this.tPDUTypeCriterion = new MTSMSTPDUTypeWrapperImpl(wrappedData);
        }
    }

    public SMSTriggerDetectionPoint getSMSTriggerDetectionPoint() {
    	if(this.smsTriggerDetectionPoint==null)
    		return null;
    	
        return this.smsTriggerDetectionPoint.getType();
    }

    public List<MTSMSTPDUType> getTPDUTypeCriterion() {
    	if(this.tPDUTypeCriterion==null)
    		return null;
    	
    	List<MTSMSTPDUType> result=new ArrayList<MTSMSTPDUType>();
    	for(ASNMTSMSTPDUType curr:this.tPDUTypeCriterion.getMTSMSTPDUType())
    		result.add(curr.getType());
    	
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MTsmsCAMELTDPCriteria [");

        if (this.smsTriggerDetectionPoint != null) {
            sb.append("smsTriggerDetectionPoint=");
            sb.append(this.smsTriggerDetectionPoint.getType());
            sb.append(", ");
        }

        if (this.tPDUTypeCriterion != null && this.tPDUTypeCriterion.getMTSMSTPDUType()!=null) {
            sb.append("mobilityTriggers=[");
            boolean firstItem = true;
            for (ASNMTSMSTPDUType be : this.tPDUTypeCriterion.getMTSMSTPDUType()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.getType());
            }
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(smsTriggerDetectionPoint==null)
			throw new ASNParsingComponentException("sms trigger detection point should be set for mt sms camel tdp criteria", ASNParsingComponentExceptionReason.MistypedParameter);

		if(tPDUTypeCriterion!=null && tPDUTypeCriterion.getMTSMSTPDUType()!=null && tPDUTypeCriterion.getMTSMSTPDUType().size()>6)
			throw new ASNParsingComponentException("tpdu criterion should be set for mt sms camel tdp criteria", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}