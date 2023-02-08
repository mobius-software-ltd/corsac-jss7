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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;

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
public class ExtBasicServiceCodeImpl implements ExtBasicServiceCode {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = ExtBearerServiceCodeImpl.class)
    private ExtBearerServiceCode extBearerService;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = ExtTeleserviceCodeImpl.class)
    private ExtTeleserviceCode extTeleservice;

    public ExtBasicServiceCodeImpl() {
    }

    public ExtBasicServiceCodeImpl(ExtBearerServiceCode extBearerService) {
        this.extBearerService = extBearerService;
    }

    public ExtBasicServiceCodeImpl(ExtTeleserviceCode extTeleservice) {
        this.extTeleservice = extTeleservice;
    }

    public ExtBearerServiceCode getExtBearerService() {
        return extBearerService;
    }

    public ExtTeleserviceCode getExtTeleservice() {
        return extTeleservice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtBasicServiceCode [");

        if (this.extBearerService != null) {
            sb.append(this.extBearerService.toString());
        }
        if (this.extTeleservice != null) {
            sb.append(this.extTeleservice.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(extBearerService==null && extTeleservice==null)
			throw new ASNParsingComponentException("ext bearer service or ext teleservice should be set for ext basic service code", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
