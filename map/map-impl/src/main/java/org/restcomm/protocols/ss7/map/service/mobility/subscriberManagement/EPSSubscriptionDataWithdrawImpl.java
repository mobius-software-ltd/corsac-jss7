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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.primitives.ASNIntegerListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EPSSubscriptionDataWithdrawImpl implements EPSSubscriptionDataWithdraw {
	private ASNNull allEpsData;
    private ASNIntegerListWrapperImpl contextIdList;

    public EPSSubscriptionDataWithdrawImpl() {
    }

    public EPSSubscriptionDataWithdrawImpl(boolean allEpsData) {
    	if(allEpsData)
    		this.allEpsData = new ASNNull();
    }

    public EPSSubscriptionDataWithdrawImpl(List<Integer> contextIdList) {
    	if(contextIdList!=null) {
    		List<ASNInteger> data=new ArrayList<ASNInteger>();
    		for(Integer curr:contextIdList) {
    			ASNInteger currData=new ASNInteger(curr,"ContextId",1,50,false);
    			data.add(currData);
    		}
    		
    		this.contextIdList = new ASNIntegerListWrapperImpl(data);
    	}
    }


    public boolean getAllEpsData() {
        return allEpsData!=null;
    }

    public List<Integer> getContextIdList() {
    	if(contextIdList==null || contextIdList.getIntegers()==null)
    		return null;
    	
    	List<Integer> data=new ArrayList<Integer>();
		for(ASNInteger curr:contextIdList.getIntegers()) {
			data.add(curr.getIntValue());
		}
		
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("EPSSubscriptionDataWithdraw [");

        if (this.allEpsData!=null) {
            sb.append("allEpsData, ");
        }
        if (this.contextIdList != null && this.contextIdList.getIntegers()!=null) {
            sb.append("contextIdList=[");
            for (ASNInteger i1 : this.contextIdList.getIntegers()) {
                sb.append(i1.getValue());
                sb.append(", ");
            }
            sb.append("], ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(allEpsData==null && contextIdList==null)
			throw new ASNParsingComponentException("either all eps data or context id list should be set for eps subscription", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
