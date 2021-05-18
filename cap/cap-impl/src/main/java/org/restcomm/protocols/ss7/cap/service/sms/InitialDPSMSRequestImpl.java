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

package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.ASNEventTypeSMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriodImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2Impl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitialDPSMSRequestImpl extends SmsMessageImpl implements InitialDPSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private CalledPartyBCDNumberImpl destinationSubscriberNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private SMSAddressStringImpl callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNEventTypeSMSImpl eventTypeSMS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1)
    private LocationInformationImpl  locationInformationMSC;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1)
    private LocationInformationGPRSImpl locationInformationGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1)
    private ISDNAddressStringImpl smscCAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1)
    private TimeAndTimezoneImpl timeAndTimezone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1)
    private TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1)
    private TPProtocolIdentifierImpl tPProtocolIdentifier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1)
    private TPDataCodingSchemeImpl tPDataCodingScheme;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false,index = -1)
    private TPValidityPeriodImpl tPValidityPeriod;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false,index = -1)
    private CallReferenceNumberImpl smsReferenceNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = false,index = -1)
    private ISDNAddressStringImpl mscAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 16,constructed = false,index = -1)
    private ISDNAddressStringImpl sgsnNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false,index = -1)
    private MSClassmark2Impl mSClassmark2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 18,constructed = true,index = -1)
    private GPRSMSClassImpl gprsMSClass;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 19,constructed = false,index = -1)
    private IMEIImpl imei;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 20,constructed = false,index = -1)
    private ISDNAddressStringImpl calledPartyNumber;

    public InitialDPSMSRequestImpl() {
        super();
    }

    public InitialDPSMSRequestImpl(int serviceKey, CalledPartyBCDNumberImpl destinationSubscriberNumber,
            SMSAddressStringImpl callingPartyNumber, EventTypeSMS eventTypeSMS, IMSIImpl imsi,
            LocationInformationImpl locationInformationMSC, LocationInformationGPRSImpl locationInformationGPRS,
            ISDNAddressStringImpl smscCAddress, TimeAndTimezoneImpl timeAndTimezone,
            TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo, TPProtocolIdentifierImpl tPProtocolIdentifier,
            TPDataCodingSchemeImpl tPDataCodingScheme, TPValidityPeriodImpl tPValidityPeriod, CAPExtensionsImpl extensions,
            CallReferenceNumberImpl smsReferenceNumber, ISDNAddressStringImpl mscAddress, ISDNAddressStringImpl sgsnNumber,
            MSClassmark2Impl mSClassmark2, GPRSMSClassImpl gprsMSClass, IMEIImpl imei, ISDNAddressStringImpl calledPartyNumber) {
        super();
        this.serviceKey = new ASNInteger();
        this.serviceKey.setValue(Long.valueOf(serviceKey));
        
        this.destinationSubscriberNumber = destinationSubscriberNumber;
        this.callingPartyNumber = callingPartyNumber;
        
        if(eventTypeSMS!=null) {
        	this.eventTypeSMS = new ASNEventTypeSMSImpl();
        	this.eventTypeSMS.setType(eventTypeSMS);
        }
        
        this.imsi = imsi;
        this.locationInformationMSC = locationInformationMSC;
        this.locationInformationGPRS = locationInformationGPRS;
        this.smscCAddress = smscCAddress;
        this.timeAndTimezone = timeAndTimezone;
        this.tPShortMessageSpecificInfo = tPShortMessageSpecificInfo;
        this.tPProtocolIdentifier = tPProtocolIdentifier;
        this.tPDataCodingScheme = tPDataCodingScheme;
        this.tPValidityPeriod = tPValidityPeriod;
        this.extensions = extensions;
        this.smsReferenceNumber = smsReferenceNumber;
        this.mscAddress = mscAddress;
        this.sgsnNumber = sgsnNumber;
        this.mSClassmark2 = mSClassmark2;
        this.gprsMSClass = gprsMSClass;
        this.imei = imei;
        this.calledPartyNumber = calledPartyNumber;
    }

    @Override
    public int getServiceKey() {
    	if(this.serviceKey==null || this.serviceKey.getValue()==null)
    		return -1;
    	
        return this.serviceKey.getValue().intValue();
    }

    @Override
    public CalledPartyBCDNumberImpl getDestinationSubscriberNumber() {
        return this.destinationSubscriberNumber;
    }

    @Override
    public SMSAddressStringImpl getCallingPartyNumber() {
        return this.callingPartyNumber;
    }

    @Override
    public EventTypeSMS getEventTypeSMS() {
    	if(eventTypeSMS==null)
    		return null;
    	
        return this.eventTypeSMS.getType();
    }

    @Override
    public IMSIImpl getImsi() {
        return this.imsi;
    }

    @Override
    public LocationInformationImpl getLocationInformationMSC() {
        return this.locationInformationMSC;
    }

    @Override
    public LocationInformationGPRSImpl getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    @Override
    public ISDNAddressStringImpl getSMSCAddress() {
        return this.smscCAddress;
    }

    @Override
    public TimeAndTimezoneImpl getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    @Override
    public TPShortMessageSpecificInfoImpl getTPShortMessageSpecificInfo() {
        return this.tPShortMessageSpecificInfo;
    }

    @Override
    public TPProtocolIdentifierImpl getTPProtocolIdentifier() {
        return this.tPProtocolIdentifier;
    }

    @Override
    public TPDataCodingSchemeImpl getTPDataCodingScheme() {
        return this.tPDataCodingScheme;
    }

    @Override
    public TPValidityPeriodImpl getTPValidityPeriod() {
        return this.tPValidityPeriod;
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return this.extensions;
    }

    @Override
    public CallReferenceNumberImpl getSmsReferenceNumber() {
        return this.smsReferenceNumber;
    }

    @Override
    public ISDNAddressStringImpl getMscAddress() {
        return this.mscAddress;
    }

    @Override
    public ISDNAddressStringImpl getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public MSClassmark2Impl getMSClassmark2() {
        return this.mSClassmark2;
    }

    @Override
    public GPRSMSClassImpl getGPRSMSClass() {
        return this.gprsMSClass;
    }

    @Override
    public IMEIImpl getImei() {
        return this.imei;
    }

    @Override
    public ISDNAddressStringImpl getCalledPartyNumber() {
        return this.calledPartyNumber;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.initialDPSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.initialDPSMS;
    }
    
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("InitialDPSMSRequest [");
        this.addInvokeIdInfo(sb);

        if(serviceKey!=null && serviceKey.getValue()!=null) {
        	sb.append(", serviceKey=");
        	sb.append(serviceKey.getValue());
        } else {
        	sb.append(", serviceKey=-1");        	
        }
        
        if (this.destinationSubscriberNumber != null) {
            sb.append(", destinationSubscriberNumber=");
            sb.append(destinationSubscriberNumber.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }
        if (this.eventTypeSMS != null && this.eventTypeSMS.getType()!=null) {
            sb.append(", eventTypeSMS=");
            sb.append(eventTypeSMS.getType());
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(imsi.toString());
        }
        if (this.locationInformationMSC != null) {
            sb.append(", locationInformationMSC=");
            sb.append(locationInformationMSC.toString());
        }
        if (this.locationInformationGPRS != null) {
            sb.append(", locationInformationGPRS=");
            sb.append(locationInformationGPRS.toString());
        }
        if (this.smscCAddress != null) {
            sb.append(", smscCAddress=");
            sb.append(smscCAddress.toString());
        }
        if (this.timeAndTimezone != null) {
            sb.append(", timeAndTimezone=");
            sb.append(timeAndTimezone.toString());
        }
        if (this.tPShortMessageSpecificInfo != null) {
            sb.append(", tPShortMessageSpecificInfo=");
            sb.append(tPShortMessageSpecificInfo.toString());
        }
        if (this.tPProtocolIdentifier != null) {
            sb.append(", tPProtocolIdentifier=");
            sb.append(tPProtocolIdentifier.toString());
        }
        if (this.tPDataCodingScheme != null) {
            sb.append(", tPDataCodingScheme=");
            sb.append(tPDataCodingScheme.toString());
        }
        if (this.tPValidityPeriod != null) {
            sb.append(", tPValidityPeriod=");
            sb.append(tPValidityPeriod.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.smsReferenceNumber != null) {
            sb.append(", smsReferenceNumber=");
            sb.append(smsReferenceNumber.toString());
        }
        if (this.mscAddress != null) {
            sb.append(", mscAddress=");
            sb.append(mscAddress.toString());
        }
        if (this.sgsnNumber != null) {
            sb.append(", sgsnNumber=");
            sb.append(sgsnNumber.toString());
        }
        if (this.mSClassmark2 != null) {
            sb.append(", mSClassmark2=");
            sb.append(mSClassmark2.toString());
        }
        if (this.gprsMSClass != null) {
            sb.append(", gprsMSClass=");
            sb.append(gprsMSClass.toString());
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(imei.toString());
        }
        if (this.calledPartyNumber != null) {
            sb.append(", calledPartyNumber=");
            sb.append(calledPartyNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
