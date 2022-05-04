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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
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
public class SendAuthenticationInfoResponseImplV1 extends MobilityMessageImpl implements SendAuthenticationInfoResponse {
	private static final long serialVersionUID = 1L;

	@ASNChoise(defaultImplementation = AuthenticationSetListV1Impl.class)
	private AuthenticationSetList authenticationSetList;
    
    public SendAuthenticationInfoResponseImplV1() {
    }
    
    public SendAuthenticationInfoResponseImplV1(AuthenticationSetList authenticationSetList) {
        this.authenticationSetList=authenticationSetList;        
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendAuthenticationInfo_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendAuthenticationInfo;
    }

    public AuthenticationSetList getAuthenticationSetList() {
        return authenticationSetList;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    public EpsAuthenticationSetList getEpsAuthenticationSetList() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendAuthenticationInfoResponse [");

        if (this.authenticationSetList != null) {
            sb.append("authenticationSetList [");
            sb.append(authenticationSetList.toString());
            sb.append("], ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(authenticationSetList==null)
			throw new ASNParsingComponentException("authentication set list should be set for send authentication info response", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
