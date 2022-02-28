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
package org.restcomm.protocols.ss7.commonapp.gap;

import org.restcomm.protocols.ss7.commonapp.api.gap.BasicGapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.CompoundCriteria;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.primitives.ScfIDImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CompoundCriteriaImpl implements CompoundCriteria {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private BasicGapCriteriaWrapperImpl basicGapCriteria;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = ScfIDImpl.class)
    private ScfID scfId;

    public CompoundCriteriaImpl() {
    }

    public CompoundCriteriaImpl(BasicGapCriteria basicGapCriteria, ScfID scfId) {
        if(basicGapCriteria!=null) {
        	this.basicGapCriteria = new BasicGapCriteriaWrapperImpl(basicGapCriteria);
        }
        
        this.scfId = scfId;
    }

    public BasicGapCriteria getBasicGapCriteria() {
    	if(basicGapCriteria==null)
    		return null;
    	
        return basicGapCriteria.getBasicGapCriteria();
    }

    public ScfID getScfID() {
        return scfId;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CompoundCriteria [");

        if (basicGapCriteria != null && basicGapCriteria.getBasicGapCriteria()!=null) {
            sb.append("basicGapCriteria=");
            sb.append(basicGapCriteria.getBasicGapCriteria());
        }

        if (scfId != null) {
            sb.append(", scfId=");
            sb.append(scfId);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(basicGapCriteria==null)
			throw new ASNParsingComponentException("basic gap criteria should be set for compound criteria", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}