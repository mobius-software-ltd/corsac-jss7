/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.ASNEventTypeSMSImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.TPDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.TPProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.TPValidityPeriodImpl;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.MSClassmark2Impl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitialDPSMSRequestImpl extends SmsMessageImpl implements InitialDPSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = CalledPartyBCDNumberImpl.class)
    private CalledPartyBCDNumber destinationSubscriberNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = SMSAddressStringImpl.class)
    private SMSAddressString callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNEventTypeSMSImpl eventTypeSMS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1,defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1,defaultImplementation = LocationInformationImpl.class)
    private LocationInformation  locationInformationMSC;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1,defaultImplementation = LocationInformationGPRSImpl.class)
    private LocationInformationGPRS locationInformationGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString smscCAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1,defaultImplementation = TimeAndTimezoneImpl.class)
    private TimeAndTimezone timeAndTimezone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1,defaultImplementation = TPShortMessageSpecificInfoImpl.class)
    private TPShortMessageSpecificInfo tPShortMessageSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1,defaultImplementation = TPProtocolIdentifierImpl.class)
    private TPProtocolIdentifier tPProtocolIdentifier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1,defaultImplementation = TPDataCodingSchemeImpl.class)
    private TPDataCodingScheme tPDataCodingScheme;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false,index = -1,defaultImplementation = TPValidityPeriodImpl.class)
    private TPValidityPeriod tPValidityPeriod;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false,index = -1,defaultImplementation = CallReferenceNumberImpl.class)
    private CallReferenceNumber smsReferenceNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = false,index = -1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mscAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 16,constructed = false,index = -1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString sgsnNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false,index = -1,defaultImplementation = MSClassmark2Impl.class)
    private MSClassmark2 mSClassmark2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 18,constructed = true,index = -1, defaultImplementation = GPRSMSClassImpl.class)
    private GPRSMSClass gprsMSClass;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 19,constructed = false,index = -1,defaultImplementation = IMEIImpl.class)
    private IMEI imei;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 20,constructed = false,index = -1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString calledPartyNumber;

    public InitialDPSMSRequestImpl() {
        super();
    }

    public InitialDPSMSRequestImpl(int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber,
            SMSAddressString callingPartyNumber, EventTypeSMS eventTypeSMS, IMSI imsi,
            LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone,
            TPShortMessageSpecificInfo tPShortMessageSpecificInfo, TPProtocolIdentifier tPProtocolIdentifier,
            TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPINAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber,
            MSClassmark2 mSClassmark2, GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) {
        super();
        this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
        this.destinationSubscriberNumber = destinationSubscriberNumber;
        this.callingPartyNumber = callingPartyNumber;
        
        if(eventTypeSMS!=null)
        	this.eventTypeSMS = new ASNEventTypeSMSImpl(eventTypeSMS);
        	
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
    	
        return this.serviceKey.getIntValue();
    }

    @Override
    public CalledPartyBCDNumber getDestinationSubscriberNumber() {
        return this.destinationSubscriberNumber;
    }

    @Override
    public SMSAddressString getCallingPartyNumber() {
        return this.callingPartyNumber;
    }

    @Override
    public EventTypeSMS getEventTypeSMS() {
    	if(eventTypeSMS==null)
    		return null;
    	
        return this.eventTypeSMS.getType();
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public LocationInformation getLocationInformationMSC() {
        return this.locationInformationMSC;
    }

    @Override
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    @Override
    public ISDNAddressString getSMSCAddress() {
        return this.smscCAddress;
    }

    @Override
    public TimeAndTimezone getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    @Override
    public TPShortMessageSpecificInfo getTPShortMessageSpecificInfo() {
        return this.tPShortMessageSpecificInfo;
    }

    @Override
    public TPProtocolIdentifier getTPProtocolIdentifier() {
        return this.tPProtocolIdentifier;
    }

    @Override
    public TPDataCodingScheme getTPDataCodingScheme() {
        return this.tPDataCodingScheme;
    }

    @Override
    public TPValidityPeriod getTPValidityPeriod() {
        return this.tPValidityPeriod;
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public CallReferenceNumber getSmsReferenceNumber() {
        return this.smsReferenceNumber;
    }

    @Override
    public ISDNAddressString getMscAddress() {
        return this.mscAddress;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public MSClassmark2 getMSClassmark2() {
        return this.mSClassmark2;
    }

    @Override
    public GPRSMSClass getGPRSMSClass() {
        return this.gprsMSClass;
    }

    @Override
    public IMEI getImei() {
        return this.imei;
    }

    @Override
    public ISDNAddressString getCalledPartyNumber() {
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(serviceKey==null)
			throw new ASNParsingComponentException("service key should be set for initial dp sms request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
