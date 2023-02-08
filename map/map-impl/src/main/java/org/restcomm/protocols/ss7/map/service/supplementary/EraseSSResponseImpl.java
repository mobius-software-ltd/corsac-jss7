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

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSInfo;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
*
* @author yulian.oifa
*
*/
@ASNWrappedTag
public class EraseSSResponseImpl extends SupplementaryMessageImpl implements EraseSSResponse {
	private static final long serialVersionUID = 1L;

	@ASNChoise(defaultImplementation = SSInfoImpl.class)
    private SSInfo ssInfo;

    public EraseSSResponseImpl() {
    }

    public EraseSSResponseImpl(SSInfo ssInfo) {
    	this.ssInfo=ssInfo;    	
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.eraseSS_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.eraseSS;
    }

    @Override
    public SSInfo getSsInfo() {
        return ssInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EraseSSResponse [");

        if (this.ssInfo != null) {
            sb.append("ssInfo=");
            sb.append(this.ssInfo);
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ssInfo==null)
			throw new ASNParsingComponentException("SS info should be set for erase SS response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
