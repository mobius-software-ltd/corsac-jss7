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

import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSInfo;

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
public class SSInfoImpl implements SSInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,constructed=true,tag=0,index=-1, defaultImplementation = ForwardingInfoImpl.class)
    private ForwardingInfo forwardingInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,constructed=true,tag=1,index=-1, defaultImplementation = CallBarringInfoImpl.class)
    private CallBarringInfo callBarringInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,constructed=true,tag=3,index=-1, defaultImplementation = SSDataImpl.class)
    private SSData ssData;

    public SSInfoImpl() {
    }

    public SSInfoImpl(ForwardingInfo forwardingInfo) {
        this.forwardingInfo = forwardingInfo;
    }

    public SSInfoImpl(CallBarringInfo callBarringInfo) {
        this.callBarringInfo = callBarringInfo;
    }

    public SSInfoImpl(SSData ssData) {
        this.ssData = ssData;
    }

    public ForwardingInfo getForwardingInfo() {
        return forwardingInfo;
    }

    public CallBarringInfo getCallBarringInfo() {
        return callBarringInfo;
    }

    public SSData getSsData() {
        return ssData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SSInfo [");

        if (this.forwardingInfo != null) {
            sb.append("forwardingInfo=");
            sb.append(this.forwardingInfo.toString());
            sb.append(", ");
        }
        if (this.callBarringInfo != null) {
            sb.append("callBarringInfo=");
            sb.append(this.callBarringInfo.toString());
            sb.append(", ");
        }
        if (this.ssData != null) {
            sb.append("ssData=");
            sb.append(this.ssData.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(forwardingInfo==null && callBarringInfo==null && ssData==null)
			throw new ASNParsingComponentException("one of child items should be set for SS info", ASNParsingComponentExceptionReason.MistypedParameter);
	}

}
