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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,lengthIndefinite=false)
public class AuthenticationSetListV3Impl implements AuthenticationSetList {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = TripletListImpl.class)
    private TripletList tripletList;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = QuintupletListImpl.class)
    private QuintupletList quintupletList;
    
    public AuthenticationSetListV3Impl() {
    }

    public AuthenticationSetListV3Impl(TripletList tripletList) {
    	this.tripletList = tripletList;    	
    }

    public AuthenticationSetListV3Impl(QuintupletList quintupletList) {
        this.quintupletList = quintupletList;
    }

    public TripletList getTripletList() {
    	return tripletList;
    }

    public QuintupletList getQuintupletList() {
        return quintupletList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationSetList [");

        if (this.tripletList != null) {
            sb.append(this.tripletList.toString());
            sb.append(", ");
        }

        if (this.quintupletList != null) {
            sb.append(this.quintupletList.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tripletList==null && quintupletList==null)
			throw new ASNParsingComponentException("only one of triplet/quintuplet should be set for authentication set list", ASNParsingComponentExceptionReason.MistypedParameter);

		if(tripletList==null || quintupletList==null)
			throw new ASNParsingComponentException("either triplet or quintuplet should be set for authentication set list", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
