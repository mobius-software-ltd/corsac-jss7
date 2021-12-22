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

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNWrappedTag
public class SendAuthenticationInfoRequestImplV1 extends MobilityMessageImpl implements SendAuthenticationInfoRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = IMSIImpl.class)
	private IMSI imsi;
    
    private long mapProtocolVersion=1;

    public SendAuthenticationInfoRequestImplV1() {
    	
    }
    
    public SendAuthenticationInfoRequestImplV1(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendAuthenticationInfoRequestImplV1(long mapProtocolVersion, IMSI imsi) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.imsi = imsi;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendAuthenticationInfo_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendAuthenticationInfo;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public int getNumberOfRequestedVectors() {
    	return 0;
    }

    public boolean getSegmentationProhibited() {
        return false;
    }

    public boolean getImmediateResponsePreferred() {
        return false;
    }

    public ReSynchronisationInfo getReSynchronisationInfo() {
        return null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    public RequestingNodeType getRequestingNodeType() {
    	return null;
    }

    public PlmnId getRequestingPlmnId() {
        return null;
    }

    public Integer getNumberOfRequestedAdditionalVectors() {
    	return null;    	
    }

    public boolean getAdditionalVectorsAreForEPS() {
        return false;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendAuthenticationInfoRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        
        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}
