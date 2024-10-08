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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TNoAnswerSpecificInfoImpl implements TNoAnswerSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private ASNNull callForwarded;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1,defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup forwardingDestinationNumber;

    public TNoAnswerSpecificInfoImpl() {
    }

    public TNoAnswerSpecificInfoImpl(boolean callForwarded, CalledPartyNumberIsup forwardingDestinationNumber) {
    	if(callForwarded)
    		this.callForwarded = new ASNNull();
    	
        this.forwardingDestinationNumber = forwardingDestinationNumber;
    }

    public boolean getCallForwarded() {
    	return callForwarded!=null;    		
    }

    public CalledPartyNumberIsup getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TNoAnswerSpecificInfo [");

        if (this.callForwarded!=null) {
            sb.append("callForwarded");
        }
        if (this.forwardingDestinationNumber != null) {
            sb.append(", forwardingDestinationNumber= [");
            sb.append(forwardingDestinationNumber);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
