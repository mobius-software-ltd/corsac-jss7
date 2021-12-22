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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author normandes
 *
 */
@ASNWrappedTag
public class CheckImeiResponseImplV1 extends MobilityMessageImpl implements CheckImeiResponse {
	private static final long serialVersionUID = 1L;

	private ASNEquipmentStatusImpl equipmentStatus;
    
    private long mapProtocolVersion;

    public CheckImeiResponseImplV1() {
    	this.mapProtocolVersion = 1;
    }
    
    // For incoming messages
    public CheckImeiResponseImplV1(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // for outgoing messages
    public CheckImeiResponseImplV1(long mapProtocolVersion, EquipmentStatus equipmentStatus) {
        this.mapProtocolVersion = mapProtocolVersion;
        
        if(equipmentStatus!=null) {
        	this.equipmentStatus = new ASNEquipmentStatusImpl();
        	this.equipmentStatus.setType(equipmentStatus);
        }
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.checkIMEI_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.checkIMEI;
    }

    @Override
    public EquipmentStatus getEquipmentStatus() {
    	if(this.equipmentStatus==null)
    		return null;
    	
        return this.equipmentStatus.getType();
    }

    @Override
    public UESBIIu getBmuef() {
        return null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CheckImeiResponse [");

        if (this.equipmentStatus != null) {
            sb.append("equipmentStatus=");
            sb.append(this.equipmentStatus.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
