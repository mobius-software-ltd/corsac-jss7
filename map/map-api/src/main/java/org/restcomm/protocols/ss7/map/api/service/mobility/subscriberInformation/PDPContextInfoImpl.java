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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PDPContextInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger pdpContextIdentifier;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull pdpContextActive;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private PDPTypeImpl pdpType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private PDPAddressImpl pdpAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private APNImpl apnSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private APNImpl apnInUse;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNInteger nsapi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private TransactionIdImpl transactionId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private TEIDImpl teidForGnAndGp;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private TEIDImpl teidForIu;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private GSNAddressImpl ggsnAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private ExtQoSSubscribedImpl qosSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private ExtQoSSubscribedImpl qosRequested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
    private ExtQoSSubscribedImpl qosNegotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
    private GPRSChargingIDImpl chargingId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)
    private ChargingCharacteristicsImpl chargingCharacteristics;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)
    private GSNAddressImpl rncAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)
    private Ext2QoSSubscribedImpl qos2Subscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)
    private Ext2QoSSubscribedImpl qos2Requested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1)
    private Ext2QoSSubscribedImpl qos2Negotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)
    private Ext3QoSSubscribedImpl qos3Subscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=false,index=-1)
    private Ext3QoSSubscribedImpl qos3Requested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
    private Ext3QoSSubscribedImpl qos3Negotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=false,index=-1)
    private Ext4QoSSubscribedImpl qos4Subscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=false,index=-1)
    private Ext4QoSSubscribedImpl qos4Requested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1)
    private Ext4QoSSubscribedImpl qos4Negotiated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=28,constructed=false,index=-1)
    private ExtPDPTypeImpl extPdpType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=29,constructed=false,index=-1)
    private PDPAddressImpl extPdpAddress;

    public PDPContextInfoImpl() {
    }

    public PDPContextInfoImpl(int pdpContextIdentifier, boolean pdpContextActive, PDPTypeImpl pdpType, PDPAddressImpl pdpAddress,
    		APNImpl apnSubscribed, APNImpl apnInUse, Integer asapi, TransactionIdImpl transactionId, TEIDImpl teidForGnAndGp, TEIDImpl teidForIu,
            GSNAddressImpl ggsnAddress, ExtQoSSubscribedImpl qosSubscribed, ExtQoSSubscribedImpl qosRequested,
            ExtQoSSubscribedImpl qosNegotiated, GPRSChargingIDImpl chargingId, ChargingCharacteristicsImpl chargingCharacteristics,
            GSNAddressImpl rncAddress, MAPExtensionContainerImpl extensionContainer, Ext2QoSSubscribedImpl qos2Subscribed,
            Ext2QoSSubscribedImpl qos2Requested, Ext2QoSSubscribedImpl qos2Negotiated, Ext3QoSSubscribedImpl qos3Subscribed,
            Ext3QoSSubscribedImpl qos3Requested, Ext3QoSSubscribedImpl qos3Negotiated, Ext4QoSSubscribedImpl qos4Subscribed,
            Ext4QoSSubscribedImpl qos4Requested, Ext4QoSSubscribedImpl qos4Negotiated, ExtPDPTypeImpl extPdpType, PDPAddressImpl extPdpAddress) {
        this.pdpContextIdentifier = new ASNInteger();
        this.pdpContextIdentifier.setValue((long)pdpContextIdentifier);
        
        if(pdpContextActive)
        	this.pdpContextActive = new ASNNull();
        
        this.pdpType = pdpType;
        this.pdpAddress = pdpAddress;
        this.apnSubscribed = apnSubscribed;
        this.apnInUse = apnInUse;
        
        if(asapi!=null) {
        	this.nsapi = new ASNInteger(); 
        	this.nsapi.setValue(asapi.longValue());
        }
        
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
    	if(pdpContextIdentifier==null)
    		return 0;
    	
        return pdpContextIdentifier.getValue().intValue();
    }

    public boolean getPdpContextActive() {
        return pdpContextActive!=null;
    }

    public PDPTypeImpl getPdpType() {
        return pdpType;
    }

    public PDPAddressImpl getPdpAddress() {
        return pdpAddress;
    }

    public APNImpl getApnSubscribed() {
        return apnSubscribed;
    }

    public APNImpl getApnInUse() {
        return apnInUse;
    }

    public Integer getNsapi() {
    	if(nsapi==null)
    		return null;
    	
        return nsapi.getValue().intValue();
    }

    public TransactionIdImpl getTransactionId() {
        return transactionId;
    }

    public TEIDImpl getTeidForGnAndGp() {
        return teidForGnAndGp;
    }

    public TEIDImpl getTeidForIu() {
        return teidForIu;
    }

    public GSNAddressImpl getGgsnAddress() {
        return ggsnAddress;
    }

    public ExtQoSSubscribedImpl getQosSubscribed() {
        return qosSubscribed;
    }

    public ExtQoSSubscribedImpl getQosRequested() {
        return qosRequested;
    }

    public ExtQoSSubscribedImpl getQosNegotiated() {
        return qosNegotiated;
    }

    public GPRSChargingIDImpl getChargingId() {
        return chargingId;
    }

    public ChargingCharacteristicsImpl getChargingCharacteristics() {
        return chargingCharacteristics;
    }

    public GSNAddressImpl getRncAddress() {
        return rncAddress;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public Ext2QoSSubscribedImpl getQos2Subscribed() {
        return qos2Subscribed;
    }

    public Ext2QoSSubscribedImpl getQos2Requested() {
        return qos2Requested;
    }

    public Ext2QoSSubscribedImpl getQos2Negotiated() {
        return qos2Negotiated;
    }

    public Ext3QoSSubscribedImpl getQos3Subscribed() {
        return qos3Subscribed;
    }

    public Ext3QoSSubscribedImpl getQos3Requested() {
        return qos3Requested;
    }

    public Ext3QoSSubscribedImpl getQos3Negotiated() {
        return qos3Negotiated;
    }

    public Ext4QoSSubscribedImpl getQos4Subscribed() {
        return qos4Subscribed;
    }

    public Ext4QoSSubscribedImpl getQos4Requested() {
        return qos4Requested;
    }

    public Ext4QoSSubscribedImpl getQos4Negotiated() {
        return qos4Negotiated;
    }

    public ExtPDPTypeImpl getExtPdpType() {
        return extPdpType;
    }

    public PDPAddressImpl getExtPdpAddress() {
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
}
