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

package org.restcomm.protocols.ss7.map.service.mobility.imei;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 * @author normandes
 *
 */
@ASNWrappedTag
public class CheckImeiRequestImplV1 extends MobilityMessageImpl implements CheckImeiRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private IMEIImpl imei = null;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private IMSIImpl imsi = null;
	
	private long mapProtocolVersion;

    // For incoming messages
    public CheckImeiRequestImplV1(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // For outgoing messages
    public CheckImeiRequestImplV1(long mapProtocolVersion, IMEIImpl imei,IMSIImpl imsi) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.imei = imei;        
        this.imsi = imsi;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.checkIMEI_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.checkIMEI;
    }

    @Override
    public IMEIImpl getIMEI() {
        return this.imei;
    }

    @Override
    public IMSIImpl getIMSI() {
        return this.imsi;
    }

    @Override
    public RequestedEquipmentInfoImpl getRequestedEquipmentInfo() {
        return null;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CheckImeiRequest [");

        if (this.imei != null) {
            sb.append("imei=");
            sb.append(imei.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}