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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.EsiGprs.DetachSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.EsiGprs.DisconnectSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.EsiGprs.PDPContextEstablishmentSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.EsiGprs.PdpContextChangeOfPositionSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PdpContextChangeOfPositionSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag=16,constructed = true,lengthIndefinite = false)
public class GPRSEventSpecificInformationImpl implements GPRSEventSpecificInformation {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = LocationInformationGPRSImpl.class)
    private LocationInformationGPRS locationInformationGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = PdpContextChangeOfPositionSpecificInformationImpl.class)
    private PdpContextChangeOfPositionSpecificInformation pdpContextchangeOfPositionSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = DetachSpecificInformationImpl.class)
    private DetachSpecificInformation detachSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = DisconnectSpecificInformationImpl.class)
    private DisconnectSpecificInformation disconnectSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1, defaultImplementation = PDPContextEstablishmentSpecificInformationImpl.class)
    private PDPContextEstablishmentSpecificInformation pdpContextEstablishmentSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1, defaultImplementation = PDPContextEstablishmentAcknowledgementSpecificInformationImpl.class)
    private PDPContextEstablishmentAcknowledgementSpecificInformation pdpContextEstablishmentAcknowledgementSpecificInformation;

    public GPRSEventSpecificInformationImpl() {
        super();
    }

    public GPRSEventSpecificInformationImpl(LocationInformationGPRS locationInformationGPRS) {
        super();
        this.locationInformationGPRS = locationInformationGPRS;
    }

    public GPRSEventSpecificInformationImpl(
            PdpContextChangeOfPositionSpecificInformation pdpContextchangeOfPositionSpecificInformation) {
        super();
        this.pdpContextchangeOfPositionSpecificInformation = pdpContextchangeOfPositionSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(DetachSpecificInformation detachSpecificInformation) {
        super();
        this.detachSpecificInformation = detachSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(DisconnectSpecificInformation disconnectSpecificInformation) {
        super();
        this.disconnectSpecificInformation = disconnectSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(
            PDPContextEstablishmentSpecificInformation pdpContextEstablishmentSpecificInformation) {
        super();
        this.pdpContextEstablishmentSpecificInformation = pdpContextEstablishmentSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(
            PDPContextEstablishmentAcknowledgementSpecificInformation pdpContextEstablishmentAcknowledgementSpecificInformation) {
        super();
        this.pdpContextEstablishmentAcknowledgementSpecificInformation = pdpContextEstablishmentAcknowledgementSpecificInformation;
    }

    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    public PdpContextChangeOfPositionSpecificInformation getPdpContextChangeOfPositionSpecificInformation() {
        return this.pdpContextchangeOfPositionSpecificInformation;
    }

    public DetachSpecificInformation getDetachSpecificInformation() {
        return this.detachSpecificInformation;
    }

    public DisconnectSpecificInformation getDisconnectSpecificInformation() {
        return this.disconnectSpecificInformation;
    }

    public PDPContextEstablishmentSpecificInformation getPDPContextEstablishmentSpecificInformation() {
        return this.pdpContextEstablishmentSpecificInformation;
    }

    public PDPContextEstablishmentAcknowledgementSpecificInformation getPDPContextEstablishmentAcknowledgementSpecificInformation() {
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(locationInformationGPRS==null && pdpContextchangeOfPositionSpecificInformation==null && detachSpecificInformation==null && disconnectSpecificInformation==null && pdpContextEstablishmentAcknowledgementSpecificInformation==null && pdpContextEstablishmentSpecificInformation==null)
			throw new ASNParsingComponentException("one of child items should be set for gprs event specific information", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}