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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;

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
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EpsAuthenticationSetListImpl implements EpsAuthenticationSetList {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = EpcAvImpl.class)
	private List<EpcAv> epcAvs;

    public EpsAuthenticationSetListImpl() {
    }

    public EpsAuthenticationSetListImpl(List<EpcAv> epcAv) {
        this.epcAvs = epcAv;
    }

    public List<EpcAv> getEpcAv() {
        return epcAvs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EpsAuthenticationSetList [");

        if (this.epcAvs != null) {
            for (EpcAv at : this.epcAvs) {
                if (at != null) {
                    sb.append(at.toString());
                    sb.append(", ");
                }
            }
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(epcAvs==null || epcAvs.size()==0)
			throw new ASNParsingComponentException("epc av list should be set for epc authentication set list", ASNParsingComponentExceptionReason.MistypedParameter);

		if(epcAvs.size()>5)
			throw new ASNParsingComponentException("epc av list size should be between 1 and 5 for epc authentication set list", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
