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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class DestinationNumberCriteriaImpl implements DestinationNumberCriteria {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNMatchType matchType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private DestinationNumberListWrapperImpl destinationNumberList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private DestinationNumberLengthListWrapperImpl destinationNumberLengthList;

    public DestinationNumberCriteriaImpl() {
    }

    public DestinationNumberCriteriaImpl(MatchType matchType, List<ISDNAddressString> destinationNumberList,
            List<Integer> destinationNumberLengthList) {
    	if(matchType!=null) {
    		this.matchType = new ASNMatchType();
    		this.matchType.setType(matchType);
    	}
    	
    	if(destinationNumberList!=null) {
    		this.destinationNumberList = new DestinationNumberListWrapperImpl(destinationNumberList);    		
    	}
    	
    	if(destinationNumberLengthList!=null) {
    		List<ASNInteger> realList=new ArrayList<ASNInteger>();
    		for(Integer curr:destinationNumberLengthList) {
    			ASNInteger wrappedCurr=new ASNInteger();
    			wrappedCurr.setValue(curr.longValue());
    			realList.add(wrappedCurr);
    		}
    		this.destinationNumberLengthList = new DestinationNumberLengthListWrapperImpl(realList);
    	}
    }

    public MatchType getMatchType() {
    	if(this.matchType==null)
    		return null;
    	
        return this.matchType.getType();
    }

    public List<ISDNAddressString> getDestinationNumberList() {
    	if(this.destinationNumberList==null)
    		return null;
    	
        return this.destinationNumberList.getDestinationNumberList();
    }

    public List<Integer> getDestinationNumberLengthList() {
    	if(this.destinationNumberLengthList==null || this.destinationNumberLengthList.getDestinationNumberLengthList()==null)
    		return null;
    	
    	List<Integer> output=new ArrayList<Integer>();
    	for(ASNInteger curr:this.destinationNumberLengthList.getDestinationNumberLengthList())
    		output.add(curr.getValue().intValue());
    	
        return output;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DestinationNumberCriteria [");

        if (this.matchType != null) {
            sb.append("matchType=");
            sb.append(this.matchType.toString());
            sb.append(", ");
        }

        if (this.destinationNumberList != null && this.destinationNumberList.getDestinationNumberList()!=null) {
            sb.append("destinationNumberList=[");
            boolean firstItem = true;
            for (ISDNAddressString be : this.destinationNumberList.getDestinationNumberList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.destinationNumberLengthList != null && this.destinationNumberLengthList.getDestinationNumberLengthList()!=null) {
            sb.append("destinationNumberLengthList=[");
            boolean firstItem = true;
            for (ASNInteger be : this.destinationNumberLengthList.getDestinationNumberLengthList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.getValue().toString());
            }
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }
}
