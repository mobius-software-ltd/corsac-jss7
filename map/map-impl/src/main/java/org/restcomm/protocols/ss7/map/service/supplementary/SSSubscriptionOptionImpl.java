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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author daniel bichara
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SSSubscriptionOptionImpl  implements SSSubscriptionOption {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNCliRestrictionOptionImpl cliRestrictionOption = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNOverrideCategoryImpl overrideCategory = null;

    public SSSubscriptionOptionImpl() {

    }

    public SSSubscriptionOptionImpl(CliRestrictionOption cliRestrictionOption) {

    	if(cliRestrictionOption!=null)
    		this.cliRestrictionOption = new ASNCliRestrictionOptionImpl(cliRestrictionOption);    		
    }

    public SSSubscriptionOptionImpl(OverrideCategory overrideCategory) {

    	if(overrideCategory!=null)
    		this.overrideCategory = new ASNOverrideCategoryImpl(overrideCategory);     		
    }

    public CliRestrictionOption getCliRestrictionOption() {
    	if(this.cliRestrictionOption==null)
    		return null;
    	
        return this.cliRestrictionOption.getType();
    }

    public OverrideCategory getOverrideCategory() {
    	if(this.overrideCategory==null)
    		return null;
    	
        return this.overrideCategory.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SSSubscriptionOption [");

        if (this.cliRestrictionOption != null) {
            sb.append("cliRestrictionOption=");
            sb.append(this.cliRestrictionOption.getType());
            sb.append(", ");
        }

        if (this.overrideCategory != null) {
            sb.append("overrideCategory=");
            sb.append(this.overrideCategory.getType());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(cliRestrictionOption==null && overrideCategory==null)
			throw new ASNParsingComponentException("one of child items should be set for SS subscription option", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
