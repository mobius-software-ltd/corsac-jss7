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

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PurgeMSRequestImplV1 extends MobilityMessageImpl implements PurgeMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
	private IMSI imsi;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString vlrNumber;
    
    private long mapProtocolVersion;

    public PurgeMSRequestImplV1() {
    	this.mapProtocolVersion=2;
    }
    
    public PurgeMSRequestImplV1(long mapProtocolVersion) {
        super();
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public PurgeMSRequestImplV1(IMSI imsi, ISDNAddressString vlrNumber, long mapProtocolVersion) {
        super();
        this.imsi = imsi;
        this.vlrNumber = vlrNumber;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.purgeMS_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.purgeMS;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressString getVlrNumber() {
        return this.vlrNumber;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
        return null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PurgeMS [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }

        if (this.vlrNumber != null) {
            sb.append("vlrNumber=");
            sb.append(vlrNumber.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
