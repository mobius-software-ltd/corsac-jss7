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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,lengthIndefinite=false)
public class SendAuthenticationInfoResponseImplV3 extends MobilityMessageImpl implements SendAuthenticationInfoResponse {
	private static final long serialVersionUID = 1L;

	@ASNChoise(defaultImplementation = AuthenticationSetListImpl.class)
    private AuthenticationSetList authenticationSetList;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = EpsAuthenticationSetListImpl.class)    
    private EpsAuthenticationSetList epsAuthenticationSetList;
    
    private long mapProtocolVersion;

    public SendAuthenticationInfoResponseImplV3() {
    	this.mapProtocolVersion=3;
    }
    
    public SendAuthenticationInfoResponseImplV3(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendAuthenticationInfoResponseImplV3(long mapProtocolVersion, AuthenticationSetList authenticationSetList,
    		MAPExtensionContainer extensionContainer, EpsAuthenticationSetList epsAuthenticationSetList) {
        this.mapProtocolVersion = mapProtocolVersion;

        this.authenticationSetList=authenticationSetList;
        this.extensionContainer = extensionContainer;
        this.epsAuthenticationSetList = epsAuthenticationSetList;
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
        return extensionContainer;
    }

    public EpsAuthenticationSetList getEpsAuthenticationSetList() {
        return epsAuthenticationSetList;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
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
        if (this.extensionContainer != null) {
            sb.append("extensionContainer [");
            sb.append(extensionContainer.toString());
            sb.append("], ");
        }
        if (this.epsAuthenticationSetList != null) {
            sb.append("epsAuthenticationSetList [");
            sb.append(epsAuthenticationSetList.toString());
            sb.append("], ");
        }

        sb.append("]");

        return sb.toString();
    }
}
