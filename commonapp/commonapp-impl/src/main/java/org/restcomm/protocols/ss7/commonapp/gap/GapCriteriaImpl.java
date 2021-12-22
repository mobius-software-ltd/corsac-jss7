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
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GapCriteriaImpl implements GapCriteria {
	@ASNChoise
	private BasicGapCriteriaImpl basicGapCriteria;
    
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1,defaultImplementation = CompoundCriteriaImpl.class)
	private CompoundCriteria compoundCriteria;

    public GapCriteriaImpl() {
    }

    public GapCriteriaImpl(BasicGapCriteria basicGapCriteria) {
    	if(basicGapCriteria!=null) {
    		if(basicGapCriteria instanceof BasicGapCriteriaImpl)
    			this.basicGapCriteria = (BasicGapCriteriaImpl)basicGapCriteria;
    		else if(basicGapCriteria.getCalledAddressAndService()!=null)
    			this.basicGapCriteria = new BasicGapCriteriaImpl(basicGapCriteria.getCalledAddressAndService());
    		else if(basicGapCriteria.getCalledAddressValue()!=null)
    			this.basicGapCriteria = new BasicGapCriteriaImpl(basicGapCriteria.getCalledAddressValue());
    		else if(basicGapCriteria.getCallingAddressAndService()!=null)
    			this.basicGapCriteria = new BasicGapCriteriaImpl(basicGapCriteria.getCallingAddressAndService());
    		else if(basicGapCriteria.getGapOnService()!=null)
    			this.basicGapCriteria = new BasicGapCriteriaImpl(basicGapCriteria.getGapOnService());
    	}
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

}
