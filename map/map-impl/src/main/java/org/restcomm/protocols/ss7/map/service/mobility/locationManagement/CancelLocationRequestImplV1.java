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
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNWrappedTag
public class CancelLocationRequestImplV1 extends MobilityMessageImpl implements CancelLocationRequest {
	private static final long serialVersionUID = 1L;

	private IMSIImpl imsi;
    private IMSIWithLMSIImpl imsiWithLmsi;
    private long mapProtocolVersion;

    public CancelLocationRequestImplV1(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public CancelLocationRequestImplV1(IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi,long mapProtocolVersion) {
        super();
        this.imsi = imsi;
        this.imsiWithLmsi = imsiWithLmsi;
        
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.cancelLocation_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.cancelLocation;
    }

    @Override
    public IMSIImpl getImsi() {
        return this.imsi;
    }

    @Override
    public IMSIWithLMSIImpl getImsiWithLmsi() {
        return this.imsiWithLmsi;
    }

    @Override
    public CancellationType getCancellationType() {
    	return null;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return null;
    }

    @Override
    public TypeOfUpdate getTypeOfUpdate() {
    	return null;
    }

    @Override
    public boolean getMtrfSupportedAndAuthorized() {
        return false;
    }

    @Override
    public boolean getMtrfSupportedAndNotAuthorized() {
        return false;
    }

    @Override
    public ISDNAddressStringImpl getNewMSCNumber() {
        return null;
    }

    @Override
    public ISDNAddressStringImpl getNewVLRNumber() {
        return null;
    }

    @Override
    public LMSIImpl getNewLmsi() {
        return null;
    }

    @Override
    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CancelLocationRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        if (this.imsiWithLmsi != null) {
            sb.append("imsiWithLmsi=");
            sb.append(imsiWithLmsi.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
