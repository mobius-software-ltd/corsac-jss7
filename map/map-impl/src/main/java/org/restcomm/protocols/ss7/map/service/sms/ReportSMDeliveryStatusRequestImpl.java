/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ReportSMDeliveryStatusRequestImpl extends SmsMessageImpl implements ReportSMDeliveryStatusRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = AddressStringImpl.class)
    private AddressString serviceCentreAddress;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=10,constructed=false,index=2)
    private ASNSMDeliveryOutcome sMDeliveryOutcome;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger absentSubscriberDiagnosticSM;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull gprsSupportIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull deliveryOutcomeIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNSMDeliveryOutcome additionalSMDeliveryOutcome;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNInteger additionalAbsentSubscriberDiagnosticSM;
    
    public ReportSMDeliveryStatusRequestImpl() {        
    }

    public ReportSMDeliveryStatusRequestImpl(ISDNAddressString msisdn,
    		AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) {
        this.msisdn = msisdn;
        this.serviceCentreAddress = serviceCentreAddress;
        
        if(sMDeliveryOutcome!=null)
        	this.sMDeliveryOutcome = new ASNSMDeliveryOutcome(sMDeliveryOutcome);
        	
        if(absentSubscriberDiagnosticSM!=null)
        	this.absentSubscriberDiagnosticSM = new ASNInteger(absentSubscriberDiagnosticSM,"AbsentSubscriberDiagnosticSM",0,12,false);
        	
        this.extensionContainer = extensionContainer;
        
        if(gprsSupportIndicator)
        	this.gprsSupportIndicator = new ASNNull();
        
        if(deliveryOutcomeIndicator)
        	this.deliveryOutcomeIndicator = new ASNNull();
        
        if(additionalSMDeliveryOutcome!=null)
        	this.additionalSMDeliveryOutcome = new ASNSMDeliveryOutcome(additionalSMDeliveryOutcome);
        	
        if(additionalAbsentSubscriberDiagnosticSM!=null)
        	this.additionalAbsentSubscriberDiagnosticSM = new ASNInteger(additionalAbsentSubscriberDiagnosticSM,"AdditionalAbsentSubscriberDiagnosticSM",0,12,false);
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.reportSM_DeliveryStatus_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.reportSM_DeliveryStatus;
    }

    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    public AddressString getServiceCentreAddress() {
        return this.serviceCentreAddress;
    }

    public SMDeliveryOutcome getSMDeliveryOutcome() {
    	if(this.sMDeliveryOutcome==null)
    		return null;
    	
        return this.sMDeliveryOutcome.getType();
    }

    public Integer getAbsentSubscriberDiagnosticSM() {
    	if(this.absentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.absentSubscriberDiagnosticSM.getIntValue();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public boolean getGprsSupportIndicator() {
        return this.gprsSupportIndicator!=null;
    }

    public boolean getDeliveryOutcomeIndicator() {
        return this.deliveryOutcomeIndicator!=null;
    }

    public SMDeliveryOutcome getAdditionalSMDeliveryOutcome() {
    	if(this.additionalSMDeliveryOutcome==null)
    		return null;
    	
        return this.additionalSMDeliveryOutcome.getType();
    }

    public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
    	if(this.additionalAbsentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.additionalAbsentSubscriberDiagnosticSM.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReportSMDeliveryStatusRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn.toString());
        }
        if (this.serviceCentreAddress != null) {
            sb.append(", serviceCentreAddress=");
            sb.append(this.serviceCentreAddress.toString());
        }
        if (this.sMDeliveryOutcome != null) {
            sb.append(", sMDeliveryOutcome=");
            sb.append(this.sMDeliveryOutcome.getType());
        }
        if (this.absentSubscriberDiagnosticSM != null) {
            sb.append(", absentSubscriberDiagnosticSM=");
            sb.append(this.absentSubscriberDiagnosticSM.getValue());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.gprsSupportIndicator!=null) {
            sb.append(", gprsSupportIndicator");
        }
        if (this.deliveryOutcomeIndicator!=null) {
            sb.append(", deliveryOutcomeIndicator");
        }
        if (this.additionalSMDeliveryOutcome != null) {
            sb.append(", additionalSMDeliveryOutcome=");
            sb.append(this.additionalSMDeliveryOutcome.getType());
        }
        if (this.additionalAbsentSubscriberDiagnosticSM != null) {
            sb.append(", additionalAbsentSubscriberDiagnosticSM=");
            sb.append(this.additionalAbsentSubscriberDiagnosticSM.getValue());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(msisdn==null)
			throw new ASNParsingComponentException("msisdn should be set for report SM delivery request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(serviceCentreAddress==null)
			throw new ASNParsingComponentException("service centre address should be set for report SM delivery request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}