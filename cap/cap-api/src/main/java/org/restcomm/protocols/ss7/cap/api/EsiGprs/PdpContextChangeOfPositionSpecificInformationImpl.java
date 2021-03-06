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
package org.restcomm.protocols.ss7.cap.api.EsiGprs;

import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true, lengthIndefinite = false)
public class PdpContextChangeOfPositionSpecificInformationImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private AccessPointNameImpl accessPointName;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private GPRSChargingIDImpl chargingID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private LocationInformationGPRSImpl locationInformationGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private EndUserAddressImpl endUserAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
    private QualityOfServiceImpl qualityOfService;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
    private TimeAndTimezoneImpl timeAndTimezone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private GSNAddressImpl gsnAddress;

    public PdpContextChangeOfPositionSpecificInformationImpl() {
    }

    public PdpContextChangeOfPositionSpecificInformationImpl(AccessPointNameImpl accessPointName, GPRSChargingIDImpl chargingID,
            LocationInformationGPRSImpl locationInformationGPRS, EndUserAddressImpl endUserAddress, QualityOfServiceImpl qualityOfService,
            TimeAndTimezoneImpl timeAndTimezone, GSNAddressImpl gsnAddress) {
        this.accessPointName = accessPointName;
        this.chargingID = chargingID;
        this.locationInformationGPRS = locationInformationGPRS;
        this.endUserAddress = endUserAddress;
        this.qualityOfService = qualityOfService;
        this.timeAndTimezone = timeAndTimezone;
        this.gsnAddress = gsnAddress;
    }

    public AccessPointNameImpl getAccessPointName() {
        return this.accessPointName;
    }

    public GPRSChargingIDImpl getChargingID() {
        return this.chargingID;
    }

    public LocationInformationGPRSImpl getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    public EndUserAddressImpl getEndUserAddress() {
        return this.endUserAddress;
    }

    public QualityOfServiceImpl getQualityOfService() {
        return this.qualityOfService;
    }

    public TimeAndTimezoneImpl getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    public GSNAddressImpl getGSNAddress() {
        return this.gsnAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PdpContextchangeOfPositionSpecificInformation [");

        if (this.accessPointName != null) {
            sb.append("accessPointName=");
            sb.append(this.accessPointName.toString());
            sb.append(", ");
        }

        if (this.chargingID != null) {
            sb.append("chargingID=");
            sb.append(this.chargingID.toString());
            sb.append(", ");
        }

        if (this.locationInformationGPRS != null) {
            sb.append("locationInformationGPRS=");
            sb.append(this.locationInformationGPRS.toString());
            sb.append(", ");
        }

        if (this.endUserAddress != null) {
            sb.append("endUserAddress=");
            sb.append(this.endUserAddress.toString());
            sb.append(", ");
        }

        if (this.qualityOfService != null) {
            sb.append("qualityOfService=");
            sb.append(this.qualityOfService.toString());
            sb.append(", ");
        }

        if (this.timeAndTimezone != null) {
            sb.append("timeAndTimezone=");
            sb.append(this.timeAndTimezone.toString());
            sb.append(", ");
        }

        if (this.gsnAddress != null) {
            sb.append("gsnAddress=");
            sb.append(this.gsnAddress.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
