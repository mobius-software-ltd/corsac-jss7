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
package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ASNGPRSEventTypeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ASNPDPInitiationTypeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitialDpGprsRequestImpl extends GprsMessageImpl implements InitialDpGprsRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNGPRSEventTypeImpl gprsEventType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = TimeAndTimezoneImpl.class)
    private TimeAndTimezone timeAndTimezone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1, defaultImplementation = GPRSMSClassImpl.class)
    private GPRSMSClass gprsMSClass;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1, defaultImplementation = EndUserAddressImpl.class)
    private EndUserAddress endUserAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1, defaultImplementation = QualityOfServiceImpl.class)
    private QualityOfService qualityOfService;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = AccessPointNameImpl.class)
    private AccessPointName accessPointName;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1, defaultImplementation = RAIdentityImpl.class)
    private RAIdentity routeingAreaIdentity;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1, defaultImplementation = GPRSChargingIDImpl.class)
    private GPRSChargingID chargingID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1, defaultImplementation = SGSNCapabilitiesImpl.class)
    private SGSNCapabilities sgsnCapabilities;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = true,index = -1, defaultImplementation = LocationInformationGPRSImpl.class)
    private LocationInformationGPRS locationInformationGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false,index = -1)
    private ASNPDPInitiationTypeImpl pdpInitiationType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = false,index = -1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress gsnAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 16,constructed = false,index = -1)
    private ASNNull secondaryPDPContext;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false,index = -1, defaultImplementation = IMEIImpl.class)
    private IMEI imei;

    public InitialDpGprsRequestImpl() {
        super();
    }

    public InitialDpGprsRequestImpl(int serviceKey, GPRSEventType gprsEventType, ISDNAddressString msisdn, IMSI imsi,
            TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, AccessPointName accessPointName, RAIdentity routeingAreaIdentity,
            GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities, LocationInformationGPRS locationInformationGPRS,
            PDPInitiationType pdpInitiationType, CAPINAPExtensions extensions, GSNAddress gsnAddress, boolean secondaryPDPContext,
            IMEI imei) {
        super();
        this.serviceKey = new ASNInteger(serviceKey);
        
        if(gprsEventType!=null)
        	this.gprsEventType = new ASNGPRSEventTypeImpl(gprsEventType);
        	
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.timeAndTimezone = timeAndTimezone;
        this.gprsMSClass = gprsMSClass;
        this.endUserAddress = endUserAddress;
        this.qualityOfService = qualityOfService;
        this.accessPointName = accessPointName;
        this.routeingAreaIdentity = routeingAreaIdentity;
        this.chargingID = chargingID;
        this.sgsnCapabilities = sgsnCapabilities;
        this.locationInformationGPRS = locationInformationGPRS;
        
        if(pdpInitiationType!=null)
        	this.pdpInitiationType = new ASNPDPInitiationTypeImpl(pdpInitiationType);
        	
        this.extensions = extensions;
        this.gsnAddress = gsnAddress;
        
        if(secondaryPDPContext)
        	this.secondaryPDPContext = new ASNNull();
        
        this.imei = imei;
    }

    @Override
    public int getServiceKey() {
    	if(this.serviceKey==null || this.serviceKey.getValue()==null)
    		return 0;
    	
        return this.serviceKey.getIntValue();
    }

    @Override
    public GPRSEventType getGPRSEventType() {
    	if(this.gprsEventType==null)
    		return null;
    	
        return this.gprsEventType.getType();
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public TimeAndTimezone getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    @Override
    public GPRSMSClass getGPRSMSClass() {
        return this.gprsMSClass;
    }

    @Override
    public EndUserAddress getEndUserAddress() {
        return this.endUserAddress;
    }

    @Override
    public QualityOfService getQualityOfService() {
        return this.qualityOfService;
    }

    @Override
    public AccessPointName getAccessPointName() {
        return this.accessPointName;
    }

    @Override
    public RAIdentity getRouteingAreaIdentity() {
        return this.routeingAreaIdentity;
    }

    @Override
    public GPRSChargingID getChargingID() {
        return this.chargingID;
    }

    @Override
    public SGSNCapabilities getSGSNCapabilities() {
        return this.sgsnCapabilities;
    }

    @Override
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    @Override
    public PDPInitiationType getPDPInitiationType() {
    	if(this.pdpInitiationType==null)
    		return null;
    	
        return this.pdpInitiationType.getType();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public GSNAddress getGSNAddress() {
        return this.gsnAddress;
    }

    @Override
    public boolean getSecondaryPDPContext() {    	
        return this.secondaryPDPContext!=null;
    }

    @Override
    public IMEI getImei() {
        return this.imei;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.initialDPGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.initialDPGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InitialDpGprsRequest [");
        this.addInvokeIdInfo(sb);

        sb.append(", serviceKey=");
        sb.append(this.getServiceKey());
        sb.append(", ");

        if (this.gprsEventType != null && this.gprsEventType.getType()!=null) {
            sb.append("gprsEventType=");
            sb.append(this.gprsEventType.getType());
            sb.append(", ");
        }

        if (this.msisdn != null) {
            sb.append("msisdn=");
            sb.append(this.msisdn.toString());
            sb.append(", ");
        }

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
            sb.append(", ");
        }

        if (this.timeAndTimezone != null) {
            sb.append("timeAndTimezone=");
            sb.append(this.timeAndTimezone.toString());
            sb.append(", ");
        }

        if (this.gprsMSClass != null) {
            sb.append("gprsMSClass=");
            sb.append(this.gprsMSClass.toString());
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

        if (this.accessPointName != null) {
            sb.append("accessPointName=");
            sb.append(this.accessPointName.toString());
            sb.append(", ");
        }

        if (this.routeingAreaIdentity != null) {
            sb.append("routeingAreaIdentity=");
            sb.append(this.routeingAreaIdentity.toString());
            sb.append(", ");
        }

        if (this.chargingID != null) {
            sb.append("chargingID=");
            sb.append(this.chargingID.toString());
            sb.append(", ");
        }

        if (this.sgsnCapabilities != null) {
            sb.append("sgsnCapabilities=");
            sb.append(this.sgsnCapabilities.toString());
            sb.append(", ");
        }

        if (this.locationInformationGPRS != null) {
            sb.append("locationInformationGPRS=");
            sb.append(this.locationInformationGPRS.toString());
            sb.append(", ");
        }

        if (this.pdpInitiationType != null && this.pdpInitiationType.getType()!=null) {
            sb.append("pdpInitiationType=");
            sb.append(this.pdpInitiationType.getType());
            sb.append(", ");
        }

        if (this.extensions != null) {
            sb.append("extensions=");
            sb.append(this.extensions.toString());
            sb.append(", ");
        }

        if (this.gsnAddress != null) {
            sb.append("gsnAddress=");
            sb.append(this.gsnAddress.toString());
            sb.append(", ");
        }

        if (this.secondaryPDPContext!=null) {
            sb.append("secondaryPDPContext ");
            sb.append(", ");
        }

        if (this.imei != null) {
            sb.append("imei=");
            sb.append(this.imei.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
