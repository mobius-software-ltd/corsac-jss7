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

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNWrappedTag
public class RegisterPasswordRequestImpl extends SupplementaryMessageImpl implements RegisterPasswordRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = SSCodeImpl.class)
	private SSCode ssCode;

    public RegisterPasswordRequestImpl() {
    }

    public RegisterPasswordRequestImpl(SSCode ssCode) {
        this.ssCode = ssCode;
    }


    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.registerPasswordRequest_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.registerPassword;
    }

    @Override
    public SSCode getSsCode() {
        return ssCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RegisterPasswordRequest [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode);
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ssCode==null)
			throw new ASNParsingComponentException("SS code should be set for register password request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
