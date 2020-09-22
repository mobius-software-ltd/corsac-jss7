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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MTsmsCAMELTDPCriteriaImpl {
	private ASNSMSTriggerDetectionPoint smsTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private MTSMSTPDUTypeWrapperImpl tPDUTypeCriterion;

    public MTsmsCAMELTDPCriteriaImpl() {
    }

    public MTsmsCAMELTDPCriteriaImpl(SMSTriggerDetectionPoint smsTriggerDetectionPoint,
            ArrayList<MTSMSTPDUType> tPDUTypeCriterion) {
        if(smsTriggerDetectionPoint!=null) {
        	this.smsTriggerDetectionPoint = new ASNSMSTriggerDetectionPoint();
        	this.smsTriggerDetectionPoint.setType(smsTriggerDetectionPoint);
        }
        
        if(tPDUTypeCriterion!=null)
        {
        	ArrayList<ASNMTSMSTPDUType> wrappedData=new ArrayList<ASNMTSMSTPDUType>();
        	for(MTSMSTPDUType curr:tPDUTypeCriterion) {
        		ASNMTSMSTPDUType item=new ASNMTSMSTPDUType();
        		item.setType(curr);
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

    public ArrayList<MTSMSTPDUType> getTPDUTypeCriterion() {
    	if(this.tPDUTypeCriterion==null)
    		return null;
    	
    	ArrayList<MTSMSTPDUType> result=new ArrayList<MTSMSTPDUType>();
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

}
