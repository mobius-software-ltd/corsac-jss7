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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribedImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PDPContextImpl {
	private ASNInteger pdpContextId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)
    private PDPTypeImpl pdpType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=false,index=-1)
    private PDPAddressImpl pdpAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)
    private QoSSubscribedImpl qosSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)
    private ASNNull vplmnAddressAllowed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1)
    private APNImpl apn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ExtQoSSubscribedImpl extQoSSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ChargingCharacteristicsImpl chargingCharacteristics;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private Ext2QoSSubscribedImpl ext2QoSSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private Ext3QoSSubscribedImpl ext3QoSSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private Ext4QoSSubscribedImpl ext4QoSSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private APNOIReplacementImpl apnoiReplacement;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ExtPDPTypeImpl extpdpType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private PDPAddressImpl extpdpAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNSIPTOPermissionImpl sipToPermission;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNLIPAPermissionImpl lipaPermission;

    public PDPContextImpl(int pdpContextId, PDPTypeImpl pdpType, PDPAddressImpl pdpAddress, QoSSubscribedImpl qosSubscribed,
            boolean vplmnAddressAllowed, APNImpl apn, MAPExtensionContainerImpl extensionContainer, ExtQoSSubscribedImpl extQoSSubscribed,
            ChargingCharacteristicsImpl chargingCharacteristics, Ext2QoSSubscribedImpl ext2QoSSubscribed,
            Ext3QoSSubscribedImpl ext3QoSSubscribed, Ext4QoSSubscribedImpl ext4QoSSubscribed, APNOIReplacementImpl apnoiReplacement,
            ExtPDPTypeImpl extpdpType, PDPAddressImpl extpdpAddress, SIPTOPermission sipToPermission, LIPAPermission lipaPermission) {
        this.pdpContextId = new ASNInteger();
        this.pdpContextId.setValue((long)pdpContextId & 0x0FFFFFFFFL);
        
        this.pdpType = pdpType;
        this.pdpAddress = pdpAddress;
        this.qosSubscribed = qosSubscribed;
        
        if(vplmnAddressAllowed)
        	this.vplmnAddressAllowed = new ASNNull();
        
        this.apn = apn;
        this.extensionContainer = extensionContainer;
        this.extQoSSubscribed = extQoSSubscribed;
        this.chargingCharacteristics = chargingCharacteristics;
        this.ext2QoSSubscribed = ext2QoSSubscribed;
        this.ext3QoSSubscribed = ext3QoSSubscribed;
        this.ext4QoSSubscribed = ext4QoSSubscribed;
        this.apnoiReplacement = apnoiReplacement;
        this.extpdpType = extpdpType;
        this.extpdpAddress = extpdpAddress;
        
        if(sipToPermission!=null) {
        	this.sipToPermission = new ASNSIPTOPermissionImpl();
        	this.sipToPermission.setType(sipToPermission);
        }
        
        if(lipaPermission!=null) {
        	this.lipaPermission = new ASNLIPAPermissionImpl();
        	this.lipaPermission.setType(lipaPermission);
        }
    }

    public PDPContextImpl(int pdpContextId) {
        this.pdpContextId = new ASNInteger();
        this.pdpContextId.setValue((long)pdpContextId & 0x0FFFFFFFFL);
    }

    public PDPContextImpl() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getPDPContextId()
     */
    public int getPDPContextId() {
    	if(this.pdpContextId==null)
    		return 0;
    	
        return this.pdpContextId.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getPDPType()
     */
    public PDPTypeImpl getPDPType() {
        return this.pdpType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getPDPAddress()
     */
    public PDPAddressImpl getPDPAddress() {
        return this.pdpAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getQoSSubscribed()
     */
    public QoSSubscribedImpl getQoSSubscribed() {
        return this.qosSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #isVPLMNAddressAllowed()
     */
    public boolean isVPLMNAddressAllowed() {
        return this.vplmnAddressAllowed!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getAPN()
     */
    public APNImpl getAPN() {
        return this.apn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtensionContainer()
     */
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtQoSSubscribed()
     */
    public ExtQoSSubscribedImpl getExtQoSSubscribed() {
        return this.extQoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getChargingCharacteristics()
     */
    public ChargingCharacteristicsImpl getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExt2QoSSubscribed()
     */
    public Ext2QoSSubscribedImpl getExt2QoSSubscribed() {
        return this.ext2QoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExt3QoSSubscribed()
     */
    public Ext3QoSSubscribedImpl getExt3QoSSubscribed() {
        return this.ext3QoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExt4QoSSubscribed()
     */
    public Ext4QoSSubscribedImpl getExt4QoSSubscribed() {
        return this.ext4QoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getAPNOIReplacement()
     */
    public APNOIReplacementImpl getAPNOIReplacement() {
        return this.apnoiReplacement;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtPDPType()
     */
    public ExtPDPTypeImpl getExtPDPType() {
        return this.extpdpType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtPDPAddress()
     */
    public PDPAddressImpl getExtPDPAddress() {
        return this.extpdpAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getSIPTOPermission()
     */
    public SIPTOPermission getSIPTOPermission() {
    	if(this.sipToPermission==null)
    		return null;
    	
        return this.sipToPermission.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getLIPAPermission()
     */
    public LIPAPermission getLIPAPermission() {
    	if(this.lipaPermission==null)
    		return null;
    	
        return this.lipaPermission.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PDPContext [");

        if(this.pdpContextId.getValue()!=null) {
	        sb.append("pdpContextId=");
	        sb.append(this.pdpContextId.getValue());
        }
        
        if (this.pdpType != null) {
            sb.append(", pdpType=");
            sb.append(this.pdpType.toString());
        }
        if (this.qosSubscribed != null) {
            sb.append(", qosSubscribed=");
            sb.append(this.qosSubscribed.toString());
        }
        if (this.vplmnAddressAllowed!=null) {
            sb.append(", vplmnAddressAllowed");
        }
        if (this.apn != null) {
            sb.append(", apn=");
            sb.append(this.apn.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.extQoSSubscribed != null) {
            sb.append(", extQoSSubscribed=");
            sb.append(this.extQoSSubscribed.toString());
        }
        if (this.chargingCharacteristics != null) {
            sb.append(", chargingCharacteristics=");
            sb.append(this.chargingCharacteristics.toString());
        }
        if (this.ext2QoSSubscribed != null) {
            sb.append(", ext2QoSSubscribed=");
            sb.append(this.ext2QoSSubscribed.toString());
        }
        if (this.ext3QoSSubscribed != null) {
            sb.append(", ext3QoSSubscribed=");
            sb.append(this.ext3QoSSubscribed.toString());
        }
        if (this.ext4QoSSubscribed != null) {
            sb.append(", ext4QoSSubscribed=");
            sb.append(this.ext4QoSSubscribed.toString());
        }
        if (this.apnoiReplacement != null) {
            sb.append(", apnoiReplacement=");
            sb.append(this.apnoiReplacement.toString());
        }
        if (this.extpdpType != null) {
            sb.append(", extpdpType=");
            sb.append(this.extpdpType.toString());
        }
        if (this.extpdpAddress != null) {
            sb.append(", extpdpAddress=");
            sb.append(this.extpdpAddress.toString());
        }
        if (this.sipToPermission != null) {
            sb.append(", sipToPermission=");
            sb.append(this.sipToPermission.getType());
        }
        if (this.lipaPermission != null) {
            sb.append(", lipaPermission=");
            sb.append(this.lipaPermission.getType());
        }

        sb.append("]");

        return sb.toString();
    }

}
