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
public class TBcsmCamelTdpCriteriaImpl {
	private ASNTBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private ExtBasicServiceCodeListWrapperImpl basicServiceCriteria;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private CauseValueWrapperImpl tCauseValueCriteria;

    public TBcsmCamelTdpCriteriaImpl() {
    }

    public TBcsmCamelTdpCriteriaImpl(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint,
            ArrayList<ExtBasicServiceCodeImpl> basicServiceCriteria, ArrayList<CauseValueImpl> tCauseValueCriteria) {
        if(tBcsmTriggerDetectionPoint!=null) {
        	this.tBcsmTriggerDetectionPoint = new ASNTBcsmTriggerDetectionPoint();
        	this.tBcsmTriggerDetectionPoint.setType(tBcsmTriggerDetectionPoint);
        }
        
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

    public ArrayList<ExtBasicServiceCodeImpl> getBasicServiceCriteria() {
    	if(this.basicServiceCriteria==null)
    		return null;
    	
        return this.basicServiceCriteria.getExtBasicServiceCodeImpl();
    }

    public ArrayList<CauseValueImpl> getTCauseValueCriteria() {
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

        if (this.basicServiceCriteria != null && this.basicServiceCriteria.getExtBasicServiceCodeImpl()!=null) {
            sb.append("basicServiceCriteria=[");
            boolean firstItem = true;
            for (ExtBasicServiceCodeImpl be : this.basicServiceCriteria.getExtBasicServiceCodeImpl()) {
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
            for (CauseValueImpl be : this.tCauseValueCriteria.getCauseValueImpl()) {
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

}
