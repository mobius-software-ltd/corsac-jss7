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

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.TMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNWrappedTag
public class SendIdentificationRequestImplV1 extends MobilityMessageImpl implements SendIdentificationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = TMSIImpl.class)
	private TMSI tmsi;
    
	private long mapProtocolVersion;

	public SendIdentificationRequestImplV1() {
		this.mapProtocolVersion = 2;
	}
	
    public SendIdentificationRequestImplV1(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendIdentificationRequestImplV1(TMSI tmsi, long mapProtocolVersion) {
        super();
        this.tmsi = tmsi;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendIdentification_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendIdentification;
    }

    @Override
    public TMSI getTmsi() {
        return this.tmsi;
    }

    @Override
    public Integer getNumberOfRequestedVectors() {
    	return null;
    }

    @Override
    public boolean getSegmentationProhibited() {
        return false;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    @Override
    public ISDNAddressString getMscNumber() {
        return null;
    }

    @Override
    public LAIFixedLength getPreviousLAI() {
        return null;
    }

    @Override
    public Integer getHopCounter() {
    	return null;
    }

    @Override
    public boolean getMtRoamingForwardingSupported() {
        return false;
    }

    @Override
    public ISDNAddressString getNewVLRNumber() {
        return null;
    }

    @Override
    public LMSI getNewLmsi() {
        return null;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendIdentificationRequest [");

        if (this.tmsi != null) {
            sb.append("tmsi=");
            sb.append(tmsi.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}