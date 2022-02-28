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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TEID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TransactionId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;

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
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PDPContextInfoImpl implements PDPContextInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger pdpContextIdentifier;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull pdpContextActive;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = PDPTypeImpl.class)
    private PDPType pdpType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = PDPAddressImpl.class)
    private PDPAddress pdpAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = APNImpl.class)
    private APN apnSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = APNImpl.class)
    private APN apnInUse;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNInteger nsapi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = TransactionIdImpl.class)
    private TransactionId transactionId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = TEIDImpl.class)
    private TEID teidForGnAndGp;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = TEIDImpl.class)
    private TEID teidForIu;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress ggsnAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1, defaultImplementation = ExtQoSSubscribedImpl.class)
    private ExtQoSSubscribed qosSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1, defaultImplementation = ExtQoSSubscribedImpl.class)
    private ExtQoSSubscribed qosRequested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1, defaultImplementation = ExtQoSSubscribedImpl.class)
    private ExtQoSSubscribed qosNegotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1, defaultImplementation = GPRSChargingIDImpl.class)
    private GPRSChargingID chargingId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1, defaultImplementation = ChargingCharacteristicsImpl.class)
    private ChargingCharacteristics chargingCharacteristics;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress rncAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1, defaultImplementation = Ext2QoSSubscribedImpl.class)
    private Ext2QoSSubscribed qos2Subscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1, defaultImplementation = Ext2QoSSubscribedImpl.class)
    private Ext2QoSSubscribed qos2Requested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1, defaultImplementation = Ext2QoSSubscribedImpl.class)
    private Ext2QoSSubscribed qos2Negotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1, defaultImplementation = Ext3QoSSubscribedImpl.class)
    private Ext3QoSSubscribed qos3Subscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=false,index=-1, defaultImplementation = Ext3QoSSubscribedImpl.class)
    private Ext3QoSSubscribed qos3Requested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=false,index=-1, defaultImplementation = Ext3QoSSubscribedImpl.class)
    private Ext3QoSSubscribed qos3Negotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=false,index=-1, defaultImplementation = Ext4QoSSubscribedImpl.class)
    private Ext4QoSSubscribed qos4Subscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=false,index=-1, defaultImplementation = Ext4QoSSubscribedImpl.class)
    private Ext4QoSSubscribed qos4Requested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1, defaultImplementation = Ext4QoSSubscribedImpl.class)
    private Ext4QoSSubscribed qos4Negotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=28,constructed=false,index=-1, defaultImplementation = ExtPDPTypeImpl.class)
    private ExtPDPType extPdpType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=29,constructed=false,index=-1, defaultImplementation = PDPAddressImpl.class)
    private PDPAddress extPdpAddress;

    public PDPContextInfoImpl() {
    }

    public PDPContextInfoImpl(int pdpContextIdentifier, boolean pdpContextActive, PDPType pdpType, PDPAddress pdpAddress,
    		APN apnSubscribed, APN apnInUse, Integer asapi, TransactionId transactionId, TEID teidForGnAndGp, TEID teidForIu,
            GSNAddress ggsnAddress, ExtQoSSubscribed qosSubscribed, ExtQoSSubscribed qosRequested,
            ExtQoSSubscribed qosNegotiated, GPRSChargingID chargingId, ChargingCharacteristics chargingCharacteristics,
            GSNAddress rncAddress, MAPExtensionContainer extensionContainer, Ext2QoSSubscribed qos2Subscribed,
            Ext2QoSSubscribed qos2Requested, Ext2QoSSubscribed qos2Negotiated, Ext3QoSSubscribed qos3Subscribed,
            Ext3QoSSubscribed qos3Requested, Ext3QoSSubscribed qos3Negotiated, Ext4QoSSubscribed qos4Subscribed,
            Ext4QoSSubscribed qos4Requested, Ext4QoSSubscribed qos4Negotiated, ExtPDPType extPdpType, PDPAddress extPdpAddress) {
        this.pdpContextIdentifier = new ASNInteger(pdpContextIdentifier,"PDPContextID",1,50,false);

        if(pdpContextActive)
        	this.pdpContextActive = new ASNNull();
        
        this.pdpType = pdpType;
        this.pdpAddress = pdpAddress;
        this.apnSubscribed = apnSubscribed;
        this.apnInUse = apnInUse;
        
        if(asapi!=null)
        	this.nsapi = new ASNInteger(asapi,"PDPContextID",0,15,false); 
        	
        this.transactionId = transactionId;
        this.teidForGnAndGp = teidForGnAndGp;
        this.teidForIu = teidForIu;
        this.ggsnAddress = ggsnAddress;
        this.qosSubscribed = qosSubscribed;
        this.qosRequested = qosRequested;
        this.qosNegotiated = qosNegotiated;
        this.chargingId = chargingId;
        this.chargingCharacteristics = chargingCharacteristics;
        this.rncAddress = rncAddress;
        this.extensionContainer = extensionContainer;
        this.qos2Subscribed = qos2Subscribed;
        this.qos2Requested = qos2Requested;
        this.qos2Negotiated = qos2Negotiated;
        this.qos3Subscribed = qos3Subscribed;
        this.qos3Requested = qos3Requested;
        this.qos3Negotiated = qos3Negotiated;
        this.qos4Subscribed = qos4Subscribed;
        this.qos4Requested = qos4Requested;
        this.qos4Negotiated = qos4Negotiated;
        this.extPdpType = extPdpType;
        this.extPdpAddress = extPdpAddress;
    }

    public int getPdpContextIdentifier() {
    	if(pdpContextIdentifier==null || pdpContextIdentifier.getValue()==null)
    		return 0;
    	
        return pdpContextIdentifier.getIntValue();
    }

    public boolean getPdpContextActive() {
        return pdpContextActive!=null;
    }

    public PDPType getPdpType() {
        return pdpType;
    }

    public PDPAddress getPdpAddress() {
        return pdpAddress;
    }

    public APN getApnSubscribed() {
        return apnSubscribed;
    }

    public APN getApnInUse() {
        return apnInUse;
    }

    public Integer getNsapi() {
    	if(nsapi==null)
    		return null;
    	
        return nsapi.getIntValue();
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public TEID getTeidForGnAndGp() {
        return teidForGnAndGp;
    }

    public TEID getTeidForIu() {
        return teidForIu;
    }

    public GSNAddress getGgsnAddress() {
        return ggsnAddress;
    }

    public ExtQoSSubscribed getQosSubscribed() {
        return qosSubscribed;
    }

    public ExtQoSSubscribed getQosRequested() {
        return qosRequested;
    }

    public ExtQoSSubscribed getQosNegotiated() {
        return qosNegotiated;
    }

    public GPRSChargingID getChargingId() {
        return chargingId;
    }

    public ChargingCharacteristics getChargingCharacteristics() {
        return chargingCharacteristics;
    }

    public GSNAddress getRncAddress() {
        return rncAddress;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public Ext2QoSSubscribed getQos2Subscribed() {
        return qos2Subscribed;
    }

    public Ext2QoSSubscribed getQos2Requested() {
        return qos2Requested;
    }

    public Ext2QoSSubscribed getQos2Negotiated() {
        return qos2Negotiated;
    }

    public Ext3QoSSubscribed getQos3Subscribed() {
        return qos3Subscribed;
    }

    public Ext3QoSSubscribed getQos3Requested() {
        return qos3Requested;
    }

    public Ext3QoSSubscribed getQos3Negotiated() {
        return qos3Negotiated;
    }

    public Ext4QoSSubscribed getQos4Subscribed() {
        return qos4Subscribed;
    }

    public Ext4QoSSubscribed getQos4Requested() {
        return qos4Requested;
    }

    public Ext4QoSSubscribed getQos4Negotiated() {
        return qos4Negotiated;
    }

    public ExtPDPType getExtPdpType() {
        return extPdpType;
    }

    public PDPAddress getExtPdpAddress() {
        return extPdpAddress;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PDPContextInfo [");

        sb.append("pdpContextIdentifier=");
        sb.append(this.pdpContextIdentifier.getValue());

        if (this.pdpContextActive!=null) {
            sb.append(", pdpContextActive");
        }
        if (this.pdpType != null) {
            sb.append(", pdpType=");
            sb.append(this.pdpType);
        }
        if (this.pdpAddress != null) {
            sb.append(", pdpAddress=");
            sb.append(this.pdpAddress);
        }
        if (this.apnSubscribed != null) {
            sb.append(", apnSubscribed=");
            sb.append(this.apnSubscribed);
        }
        if (this.apnInUse != null) {
            sb.append(", apnInUse=");
            sb.append(this.apnInUse);
        }
        if (this.nsapi != null) {
            sb.append(", nsapi=");
            sb.append(this.nsapi.getValue());
        }
        if (this.transactionId != null) {
            sb.append(", transactionId=");
            sb.append(this.transactionId);
        }
        if (this.teidForGnAndGp != null) {
            sb.append(", teidForGnAndGp=");
            sb.append(this.teidForGnAndGp);
        }
        if (this.teidForIu != null) {
            sb.append(", teidForIu=");
            sb.append(this.teidForIu);
        }
        if (this.ggsnAddress != null) {
            sb.append(", ggsnAddress=");
            sb.append(this.ggsnAddress);
        }
        if (this.qosSubscribed != null) {
            sb.append(", qosSubscribed=");
            sb.append(this.qosSubscribed);
        }
        if (this.qosRequested != null) {
            sb.append(", qosRequested=");
            sb.append(this.qosRequested);
        }
        if (this.qosNegotiated != null) {
            sb.append(", qosNegotiated=");
            sb.append(this.qosNegotiated);
        }
        if (this.chargingId != null) {
            sb.append(", chargingId=");
            sb.append(this.chargingId);
        }
        if (this.chargingCharacteristics != null) {
            sb.append(", chargingCharacteristics=");
            sb.append(this.chargingCharacteristics);
        }
        if (this.rncAddress != null) {
            sb.append(", rncAddress=");
            sb.append(this.rncAddress);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.qos2Subscribed != null) {
            sb.append(", qos2Subscribed=");
            sb.append(this.qos2Subscribed);
        }
        if (this.qos2Requested != null) {
            sb.append(", qos2Requested=");
            sb.append(this.qos2Requested);
        }
        if (this.qos2Negotiated != null) {
            sb.append(", qos2Negotiated=");
            sb.append(this.qos2Negotiated);
        }
        if (this.qos3Subscribed != null) {
            sb.append(", qos3Subscribed=");
            sb.append(this.qos3Subscribed);
        }
        if (this.qos3Requested != null) {
            sb.append(", qos3Requested=");
            sb.append(this.qos3Requested);
        }
        if (this.qos3Negotiated != null) {
            sb.append(", qos3Negotiated=");
            sb.append(this.qos3Negotiated);
        }
        if (this.qos4Subscribed != null) {
            sb.append(", qos4Subscribed=");
            sb.append(this.qos4Subscribed);
        }
        if (this.qos4Requested != null) {
            sb.append(", qos4Requested=");
            sb.append(this.qos4Requested);
        }
        if (this.qos4Negotiated != null) {
            sb.append(", qos4Negotiated=");
            sb.append(this.qos4Negotiated);
        }
        if (this.extPdpType != null) {
            sb.append(", extPdpType=");
            sb.append(this.extPdpType);
        }
        if (this.extPdpAddress != null) {
            sb.append(", extPdpAddress=");
            sb.append(this.extPdpAddress);
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(pdpContextIdentifier==null)
			throw new ASNParsingComponentException("pdp context id should be set for pdp context info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(pdpType==null)
			throw new ASNParsingComponentException("pdp type should be set for pdp context", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
