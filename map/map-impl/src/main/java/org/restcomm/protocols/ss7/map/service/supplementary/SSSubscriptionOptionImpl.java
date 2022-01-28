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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author daniel bichara
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

}
