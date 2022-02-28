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
