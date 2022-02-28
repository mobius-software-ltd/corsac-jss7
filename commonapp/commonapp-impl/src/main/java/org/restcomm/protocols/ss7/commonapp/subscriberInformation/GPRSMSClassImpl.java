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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSNetworkCapability;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSRadioAccessCapability;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author abhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSMSClassImpl implements GPRSMSClass {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = MSNetworkCapabilityImpl.class)
    private MSNetworkCapability mSNetworkCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = MSRadioAccessCapabilityImpl.class)
    private MSRadioAccessCapability mSRadioAccessCapability;

    /**
     *
     */
    public GPRSMSClassImpl() {
    }

    public GPRSMSClassImpl(MSNetworkCapability mSNetworkCapability, MSRadioAccessCapability mSRadioAccessCapability) {
        this.mSNetworkCapability = mSNetworkCapability;
        this.mSRadioAccessCapability = mSRadioAccessCapability;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.GPRSMSClass #getMSNetworkCapability()
     */
    public MSNetworkCapability getMSNetworkCapability() {
        return this.mSNetworkCapability;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.GPRSMSClass #getMSRadioAccessCapability()
     */
    public MSRadioAccessCapability getMSRadioAccessCapability() {
        return this.mSRadioAccessCapability;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSMSClass [");

        if (this.mSNetworkCapability != null) {
            sb.append("mSNetworkCapability=");
            sb.append(this.mSNetworkCapability);
        }

        if (this.mSRadioAccessCapability != null) {
            sb.append(", mSRadioAccessCapability=");
            sb.append(this.mSRadioAccessCapability);
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(mSNetworkCapability==null)
			throw new ASNParsingComponentException("ms network capability should be set for gprs ms class", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}