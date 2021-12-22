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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNWrappedTag
public class ProvideRoamingNumberResponseImplV1 extends CallHandlingMessageImpl implements ProvideRoamingNumberResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
	public ISDNAddressString roamingNumber;    
    
	private long mapProtocolVersion;
    
    public ProvideRoamingNumberResponseImplV1() {
    	this.mapProtocolVersion=2;
    }
    
    public ProvideRoamingNumberResponseImplV1(ISDNAddressString roamingNumber, long mapProtocolVersion) {
        super();
        this.roamingNumber = roamingNumber;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public ProvideRoamingNumberResponseImplV1(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.privideRoamingNumber_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.provideRoamingNumber;
    }

    @Override
    public ISDNAddressString getRoamingNumber() {
        return this.roamingNumber;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    @Override
    public boolean getReleaseResourcesSupported() {
        return false;
    }

    @Override
    public ISDNAddressString getVmscAddress() {
        return null;
    }

    @Override
    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProvideRoamingNumberResponse [");

        if (this.roamingNumber != null) {
            sb.append("roamingNumber=");
            sb.append(roamingNumber.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}