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

package org.restcomm.protocols.ss7.commonapp.gap;

import org.restcomm.protocols.ss7.commonapp.api.gap.BasicGapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.CompoundCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 * @author yulianoifa
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GapCriteriaImpl implements GapCriteria {
	@ASNChoise(defaultImplementation = BasicGapCriteriaImpl.class)
	private BasicGapCriteria basicGapCriteria;
    
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1,defaultImplementation = CompoundCriteriaImpl.class)
	private CompoundCriteria compoundCriteria;

    public GapCriteriaImpl() {
    }

    public GapCriteriaImpl(BasicGapCriteria basicGapCriteria) {
    	this.basicGapCriteria = basicGapCriteria;    		
    }

    public GapCriteriaImpl(CompoundCriteria compoundCriteria) {
        this.compoundCriteria = compoundCriteria;
    }

    public BasicGapCriteria getBasicGapCriteria() {
        return basicGapCriteria;
    }

    public CompoundCriteria getCompoundGapCriteria() {
        return compoundCriteria;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("GapCriteria [");

        if (basicGapCriteria != null) {
            sb.append("basicGapCriteria=");
            sb.append(basicGapCriteria);
        } else if (compoundCriteria != null) {
            sb.append("compoundCriteria=");
            sb.append(compoundCriteria);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(basicGapCriteria==null && compoundCriteria==null)
			throw new ASNParsingComponentException("either basic gap criteria or compound criteria should be set for gap criteria", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
