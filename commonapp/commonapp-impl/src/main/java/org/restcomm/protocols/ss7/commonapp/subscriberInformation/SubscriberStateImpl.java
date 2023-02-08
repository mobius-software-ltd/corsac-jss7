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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberStateChoice;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SubscriberStateImpl implements SubscriberState {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull assumeIdle;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull camelBusy;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull notProvisionedFromVLR;
    
    private ASNNotReachableReasonImpl notReachableReason;

    public SubscriberStateImpl() {
    }

    public SubscriberStateImpl(SubscriberStateChoice subscriberStateChoice, NotReachableReason notReachableReason) {
        setData(subscriberStateChoice, notReachableReason);
    }

    public void setData(SubscriberStateChoice subscriberStateChoice, NotReachableReason notReachableReason) {
        switch (subscriberStateChoice) {
			case assumedIdle:
				this.assumeIdle=new ASNNull();
				break;
			case camelBusy:
				this.camelBusy=new ASNNull();
				break;
			case netDetNotReachable:
				if(notReachableReason!=null)
					this.notReachableReason = new ASNNotReachableReasonImpl(notReachableReason);					
				break;
			case notProvidedFromVLR:
				this.notProvisionedFromVLR=new ASNNull();
				break;
			default:
				break;        
		}        
    }

    public SubscriberStateChoice getSubscriberStateChoice() {
    	if(assumeIdle!=null)
    		return SubscriberStateChoice.assumedIdle;
    	else if(camelBusy!=null)
    		return SubscriberStateChoice.camelBusy;
    	else if(notProvisionedFromVLR!=null)
    		return SubscriberStateChoice.notProvidedFromVLR;
    	else if(this.notReachableReason!=null)
    		return SubscriberStateChoice.netDetNotReachable;
        
    	return null;    	
    }

    public NotReachableReason getNotReachableReason() {
    	if(this.notReachableReason==null)
    		return null;
    	
        return notReachableReason.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubscriberState [");

        SubscriberStateChoice subscriberStateChoice=getSubscriberStateChoice();
        if (subscriberStateChoice != null) {
            sb.append("subscriberStateChoice=");
            sb.append(subscriberStateChoice);
        }
        
        if (this.notReachableReason != null) {
            sb.append(", notReachableReason=");
            sb.append(this.notReachableReason);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(assumeIdle==null && camelBusy==null && notProvisionedFromVLR==null && notReachableReason==null)
			throw new ASNParsingComponentException("one of child parameters should be set for subscriber state", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
