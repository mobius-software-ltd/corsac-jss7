/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.ASNSMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.ASNSM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationIDImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author eva ogallar
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInfoForSMRequestImpl extends SmsMessageImpl implements SendRoutingInfoForSMRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ISDNAddressStringImpl msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1)
    private ASNBoolean sm_RP_PRI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=2)
    private AddressStringImpl serviceCentreAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull gprsSupportIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNSM_RP_MTI sM_RP_MTI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private SM_RP_SMEAImpl sM_RP_SMEA;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private TeleserviceCodeImpl teleservice;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private ASNNull ipSmGwGuidanceIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNSMDeliveryNotIntended smDeliveryNotIntended;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
    private ASNNull t4TriggerIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
    private ASNNull singleAttemptDelivery;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=true,index=-1)
    private CorrelationIDImpl correlationID;

    public SendRoutingInfoForSMRequestImpl() {
    }

    public SendRoutingInfoForSMRequestImpl(ISDNAddressStringImpl msisdn, boolean sm_RP_PRI, AddressStringImpl serviceCentreAddress,
            MAPExtensionContainerImpl extensionContainer, boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, SM_RP_SMEAImpl sM_RP_SMEA,
            SMDeliveryNotIntended smDeliveryNotIntended, boolean ipSmGwGuidanceIndicator, IMSIImpl imsi, boolean t4TriggerIndicator,
            boolean singleAttemptDelivery, TeleserviceCodeImpl teleservice, CorrelationIDImpl correlationID) {
        this.msisdn = msisdn;
        
        this.sm_RP_PRI = new ASNBoolean();
        this.sm_RP_PRI.setValue(sm_RP_PRI);
        
        this.serviceCentreAddress = serviceCentreAddress;
        this.extensionContainer = extensionContainer;
        
        if(gprsSupportIndicator)
        	this.gprsSupportIndicator = new ASNNull();
        
        if(sM_RP_MTI!=null) {
        	this.sM_RP_MTI = new ASNSM_RP_MTI();
        	this.sM_RP_MTI.setType(sM_RP_MTI);
        }
        
        this.sM_RP_SMEA = sM_RP_SMEA;
        
        if(smDeliveryNotIntended!=null) {
        	this.smDeliveryNotIntended = new ASNSMDeliveryNotIntended();
        	this.smDeliveryNotIntended.setType(smDeliveryNotIntended);
        }
        
        if(ipSmGwGuidanceIndicator)
        	this.ipSmGwGuidanceIndicator = new ASNNull();
        this.imsi = imsi;
        
        if(t4TriggerIndicator)
        	this.t4TriggerIndicator = new ASNNull();
        
        if(singleAttemptDelivery)
        	this.singleAttemptDelivery = new ASNNull();
        
        this.teleservice = teleservice;
        this.correlationID = correlationID;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForSM_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForSM;
    }

    public ISDNAddressStringImpl getMsisdn() {
        return this.msisdn;
    }

    public boolean getSm_RP_PRI() {
    	if(this.sm_RP_PRI==null)
    		return false;
    	
        return this.sm_RP_PRI.getValue();
    }

    public AddressStringImpl getServiceCentreAddress() {
        return this.serviceCentreAddress;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public boolean getGprsSupportIndicator() {
        return this.gprsSupportIndicator!=null;
    }

    public SM_RP_MTI getSM_RP_MTI() {
    	if(this.sM_RP_MTI==null)
    		return null;
    	
        return this.sM_RP_MTI.getType();
    }

    public SM_RP_SMEAImpl getSM_RP_SMEA() {
        return this.sM_RP_SMEA;
    }

    public TeleserviceCodeImpl getTeleservice() {
        return this.teleservice;
    }

    public boolean getIpSmGwGuidanceIndicator() {
        return ipSmGwGuidanceIndicator!=null;
    }

    public boolean getT4TriggerIndicator() {
        return t4TriggerIndicator!=null;
    }

    public boolean getSingleAttemptDelivery() {
        return singleAttemptDelivery!=null;
    }

    public IMSIImpl getImsi() {
        return imsi;
    }

    public SMDeliveryNotIntended getSmDeliveryNotIntended() {
    	if(smDeliveryNotIntended==null)
    		return null;
    	
        return smDeliveryNotIntended.getType();
    }

    public CorrelationIDImpl getCorrelationID() {
        return correlationID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInfoForSMRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn.toString());
        }
        if (this.sm_RP_PRI!=null)
            sb.append(", sm_RP_PRI=" + this.sm_RP_PRI.getValue());
        
        if (this.serviceCentreAddress != null) {
            sb.append(", serviceCentreAddress=");
            sb.append(this.serviceCentreAddress.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        
        if (this.gprsSupportIndicator!=null) {
            sb.append(", gprsSupportIndicator");
        }
        
        if (this.sM_RP_MTI != null) {
            sb.append(", sM_RP_MTI=");
            sb.append(this.sM_RP_MTI.toString());
        }
        if (this.sM_RP_SMEA != null) {
            sb.append(", sM_RP_SMEA=");
            sb.append(this.sM_RP_SMEA.toString());
        }

        if (this.smDeliveryNotIntended != null) {
            sb.append(", smDeliveryNotIntended=");
            sb.append(this.smDeliveryNotIntended.toString());
        }
        
        if (this.ipSmGwGuidanceIndicator!=null) {
            sb.append(", ipSmGwGuidanceIndicator");
        }
        
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi.toString());
        }
        
        if (this.t4TriggerIndicator!=null) {
            sb.append(", t4TriggerIndicator");
        }
        
        if (this.singleAttemptDelivery!=null) {
            sb.append(", singleAttemptDelivery");
        }

        if (this.teleservice != null) {
            sb.append(", teleservice=");
            sb.append(this.teleservice.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}