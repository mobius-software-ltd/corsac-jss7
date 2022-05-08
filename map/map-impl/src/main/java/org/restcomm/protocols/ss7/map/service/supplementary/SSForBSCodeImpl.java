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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SSForBSCodeImpl implements SSForBSCode {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = SSCodeImpl.class)
	private SSCode ssCode;
    
    @ASNChoise(defaultImplementation = BasicServiceCodeImpl.class)
    private BasicServiceCode basicServiceCode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNNull longFtnSupported;

    public SSForBSCodeImpl() {
    }

    public SSForBSCodeImpl(SSCode ssCode, BasicServiceCode basicServiceCode, boolean longFtnSupported) {
        this.ssCode = ssCode;
        this.basicServiceCode=basicServiceCode;
    	
        if(longFtnSupported)
        	this.longFtnSupported = new ASNNull();
    }

    public SSCode getSsCode() {
        return ssCode;
    }

    public BasicServiceCode getBasicService() {
        return basicServiceCode;
    }

    public boolean getLongFtnSupported() {
        return longFtnSupported!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SSForBSCode [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(ssCode);
            sb.append(", ");
        }
        if (this.basicServiceCode != null) {
            sb.append("basicService=");
            sb.append(basicServiceCode);
            sb.append(", ");
        }
        if (this.longFtnSupported!=null) {
            sb.append("longFtnSupported, ");
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ssCode==null)
			throw new ASNParsingComponentException("SS info should be set for register SS response", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}