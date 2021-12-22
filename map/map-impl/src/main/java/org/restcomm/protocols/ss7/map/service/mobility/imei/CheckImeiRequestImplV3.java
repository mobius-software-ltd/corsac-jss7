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

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author normandes
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CheckImeiRequestImplV3 extends MobilityMessageImpl implements CheckImeiRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = IMEIImpl.class)
	private IMEI imei = null;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=1, defaultImplementation = RequestedEquipmentInfoImpl.class)
	private RequestedEquipmentInfo requestedEquipmentInfo = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer = null;

    private long mapProtocolVersion;

    public CheckImeiRequestImplV3() {
    	this.mapProtocolVersion = 3;
    }
    
    // For incoming messages
    public CheckImeiRequestImplV3(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // For outgoing messages
    public CheckImeiRequestImplV3(long mapProtocolVersion, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.imei = imei;
        this.requestedEquipmentInfo = requestedEquipmentInfo;
        this.extensionContainer = extensionContainer;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    public IMSI getIMSI() {
        return null;
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
    public IMEI getIMEI() {
        return this.imei;
    }

    @Override
    public RequestedEquipmentInfo getRequestedEquipmentInfo() {
        return this.requestedEquipmentInfo;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
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

        if (this.requestedEquipmentInfo != null) {
            sb.append("requestedEquipmentInfo=");
            sb.append(requestedEquipmentInfo.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
