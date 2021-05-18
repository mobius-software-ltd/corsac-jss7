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
package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PdpContextChangeOfPositionSpecificInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag=16,constructed = true,lengthIndefinite = false)
public class GPRSEventSpecificInformationImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private LocationInformationGPRSImpl locationInformationGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private PdpContextChangeOfPositionSpecificInformationImpl pdpContextchangeOfPositionSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private DetachSpecificInformationImpl detachSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private DisconnectSpecificInformationImpl disconnectSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
    private PDPContextEstablishmentSpecificInformationImpl pdpContextEstablishmentSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1)
    private PDPContextEstablishmentAcknowledgementSpecificInformationImpl pdpContextEstablishmentAcknowledgementSpecificInformation;

    public GPRSEventSpecificInformationImpl() {
        super();
    }

    public GPRSEventSpecificInformationImpl(LocationInformationGPRSImpl locationInformationGPRS) {
        super();
        this.locationInformationGPRS = locationInformationGPRS;
    }

    public GPRSEventSpecificInformationImpl(
            PdpContextChangeOfPositionSpecificInformationImpl pdpContextchangeOfPositionSpecificInformation) {
        super();
        this.pdpContextchangeOfPositionSpecificInformation = pdpContextchangeOfPositionSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(DetachSpecificInformationImpl detachSpecificInformation) {
        super();
        this.detachSpecificInformation = detachSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(DisconnectSpecificInformationImpl disconnectSpecificInformation) {
        super();
        this.disconnectSpecificInformation = disconnectSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(
            PDPContextEstablishmentSpecificInformationImpl pdpContextEstablishmentSpecificInformation) {
        super();
        this.pdpContextEstablishmentSpecificInformation = pdpContextEstablishmentSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(
            PDPContextEstablishmentAcknowledgementSpecificInformationImpl pdpContextEstablishmentAcknowledgementSpecificInformation) {
        super();
        this.pdpContextEstablishmentAcknowledgementSpecificInformation = pdpContextEstablishmentAcknowledgementSpecificInformation;
    }

    public LocationInformationGPRSImpl getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    public PdpContextChangeOfPositionSpecificInformationImpl getPdpContextchangeOfPositionSpecificInformation() {
        return this.pdpContextchangeOfPositionSpecificInformation;
    }

    public DetachSpecificInformationImpl getDetachSpecificInformation() {
        return this.detachSpecificInformation;
    }

    public DisconnectSpecificInformationImpl getDisconnectSpecificInformation() {
        return this.disconnectSpecificInformation;
    }

    public PDPContextEstablishmentSpecificInformationImpl getPDPContextEstablishmentSpecificInformation() {
        return this.pdpContextEstablishmentSpecificInformation;
    }

    public PDPContextEstablishmentAcknowledgementSpecificInformationImpl getPDPContextEstablishmentAcknowledgementSpecificInformation() {
        return this.pdpContextEstablishmentAcknowledgementSpecificInformation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSEventSpecificInformation [");

        if (this.locationInformationGPRS != null) {
            sb.append("locationInformationGPRS=");
            sb.append(this.locationInformationGPRS.toString());
            sb.append(" ");
        } else if (this.pdpContextchangeOfPositionSpecificInformation != null) {
            sb.append("pdpContextchangeOfPositionSpecificInformation=");
            sb.append(this.pdpContextchangeOfPositionSpecificInformation.toString());
            sb.append(" ");
        } else if (this.detachSpecificInformation != null) {
            sb.append("detachSpecificInformation=");
            sb.append(this.detachSpecificInformation.toString());
            sb.append(" ");
        } else if (this.disconnectSpecificInformation != null) {
            sb.append("disconnectSpecificInformation=");
            sb.append(this.disconnectSpecificInformation.toString());
            sb.append(" ");
        } else if (this.pdpContextEstablishmentSpecificInformation != null) {
            sb.append("pdpContextEstablishmentSpecificInformation=");
            sb.append(this.pdpContextEstablishmentSpecificInformation.toString());
            sb.append(" ");
        } else if (this.pdpContextEstablishmentAcknowledgementSpecificInformation != null) {
            sb.append("pdpContextEstablishmentAcknowledgementSpecificInformation=");
            sb.append(this.pdpContextEstablishmentAcknowledgementSpecificInformation.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
