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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendIdentificationResponseImplV1 extends MobilityMessageImpl implements SendIdentificationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index = -1, defaultImplementation = IMSIImpl.class)
	private IMSI imsi;
    
    @ASNChoise
    private AuthenticationSetListImpl authenticationSetList;
    
    private long mapProtocolVersion;

    public SendIdentificationResponseImplV1() {
    	this.mapProtocolVersion = 2;
    }
    
    public SendIdentificationResponseImplV1(long mapProtocolVersion) {
        super();
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendIdentificationResponseImplV1(IMSI imsi, AuthenticationSetList authenticationSetList, long mapProtocolVersion) {
        super();
        this.imsi = imsi;

        if(authenticationSetList instanceof AuthenticationSetListImpl)
        	this.authenticationSetList=(AuthenticationSetListImpl)authenticationSetList;
        if(authenticationSetList!=null) {
        	if(authenticationSetList.getQuintupletList()!=null)
        		this.authenticationSetList = new AuthenticationSetListImpl(authenticationSetList.getQuintupletList());
        	else if(authenticationSetList.getTripletList()!=null)
        		this.authenticationSetList = new AuthenticationSetListImpl(authenticationSetList.getTripletList(), mapProtocolVersion);
        }
        
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendIdentification_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendIdentification;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public AuthenticationSetList getAuthenticationSetList() {
        return this.authenticationSetList;
    }

    @Override
    public CurrentSecurityContext getCurrentSecurityContext() {
        return null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendIdentificationResponse [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
            sb.append(", ");
        }

        if (this.authenticationSetList != null) {
            sb.append("authenticationSetList=");
            sb.append(this.authenticationSetList.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
