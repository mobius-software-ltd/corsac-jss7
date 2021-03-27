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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ASNLIPAPermissionImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ASNSIPTOPermissionImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;

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
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class APNConfigurationImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger contextId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private PDNTypeImpl pDNType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private PDPAddressImpl servedPartyIPIPv4Address;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private APNImpl apn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private EPSQoSSubscribedImpl ePSQoSSubscribed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private PDNGWIdentityImpl pdnGwIdentity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNPDNGWAllocationType pdnGwAllocationType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull vplmnAddressAllowed;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ChargingCharacteristicsImpl chargingCharacteristics;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private AMBRImpl ambr;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private SpecificAPNInfoListWrapperImpl specificAPNInfoList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private PDPAddressImpl servedPartyIPIPv6Address;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
    private APNOIReplacementImpl apnOiReplacement;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
    private ASNSIPTOPermissionImpl siptoPermission;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)
    private ASNLIPAPermissionImpl lipaPermission;

    public APNConfigurationImpl() {
    }

    public APNConfigurationImpl(int contextId, PDNTypeImpl pDNType, PDPAddressImpl servedPartyIPIPv4Address, APNImpl apn,
            EPSQoSSubscribedImpl ePSQoSSubscribed, PDNGWIdentityImpl pdnGwIdentity, PDNGWAllocationType pdnGwAllocationType,
            boolean vplmnAddressAllowed, ChargingCharacteristicsImpl chargingCharacteristics, AMBRImpl ambr,
            List<SpecificAPNInfoImpl> specificAPNInfoList, MAPExtensionContainerImpl extensionContainer,
            PDPAddressImpl servedPartyIPIPv6Address, APNOIReplacementImpl apnOiReplacement, SIPTOPermission siptoPermission,
            LIPAPermission lipaPermission) {
        this.contextId = new ASNInteger();
        this.contextId.setValue((long)contextId & 0x0FFFFFFFFL);
        this.pDNType = pDNType;
        this.servedPartyIPIPv4Address = servedPartyIPIPv4Address;
        this.apn = apn;
        this.ePSQoSSubscribed = ePSQoSSubscribed;
        this.pdnGwIdentity = pdnGwIdentity;
        
        if(pdnGwAllocationType!=null) {
        	this.pdnGwAllocationType = new ASNPDNGWAllocationType();
        	this.pdnGwAllocationType.setType(pdnGwAllocationType);
        }
        
        if(vplmnAddressAllowed)
        	this.vplmnAddressAllowed = new ASNNull();
        
        this.chargingCharacteristics = chargingCharacteristics;
        this.ambr = ambr;
        
        if(specificAPNInfoList!=null)
        	this.specificAPNInfoList = new SpecificAPNInfoListWrapperImpl(specificAPNInfoList);
        
        this.extensionContainer = extensionContainer;
        this.servedPartyIPIPv6Address = servedPartyIPIPv6Address;
        this.apnOiReplacement = apnOiReplacement;
        
        if(siptoPermission!=null) {
        	this.siptoPermission = new ASNSIPTOPermissionImpl();
        	this.siptoPermission.setType(siptoPermission);
        }
        
        if(lipaPermission!=null) {
        	this.lipaPermission = new ASNLIPAPermissionImpl();
        	this.lipaPermission.setType(lipaPermission);
        }
    }

    public int getContextId() {
    	if(this.contextId==null)
    		return -1;
    	
        return this.contextId.getValue().intValue();
    }

    public PDNTypeImpl getPDNType() {
        return this.pDNType;
    }

    public PDPAddressImpl getServedPartyIPIPv4Address() {
        return this.servedPartyIPIPv4Address;
    }

    public APNImpl getApn() {
        return this.apn;
    }

    public EPSQoSSubscribedImpl getEPSQoSSubscribed() {
        return this.ePSQoSSubscribed;
    }

    public PDNGWIdentityImpl getPdnGwIdentity() {
        return this.pdnGwIdentity;
    }

    public PDNGWAllocationType getPdnGwAllocationType() {
    	if(this.pdnGwAllocationType==null)
    		return null;
    	
        return this.pdnGwAllocationType.getType();
    }

    public boolean getVplmnAddressAllowed() {
        return this.vplmnAddressAllowed!=null;
    }

    public ChargingCharacteristicsImpl getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    public AMBRImpl getAmbr() {
        return this.ambr;
    }

    public List<SpecificAPNInfoImpl> getSpecificAPNInfoList() {
    	if(this.specificAPNInfoList==null)
    		return null;
    	
        return this.specificAPNInfoList.getSpecificAPNInfo();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public PDPAddressImpl getServedPartyIPIPv6Address() {
        return this.servedPartyIPIPv6Address;
    }

    public APNOIReplacementImpl getApnOiReplacement() {
        return this.apnOiReplacement;
    }

    public SIPTOPermission getSiptoPermission() {
    	if(this.siptoPermission==null)
    		return null;
    	
        return this.siptoPermission.getType();
    }

    public LIPAPermission getLipaPermission() {
    	if(this.lipaPermission==null)
    		return null;
    	
        return lipaPermission.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("APNConfiguration [");

        sb.append("contextId=");
        sb.append(this.contextId);
        sb.append(", ");

        if (this.pDNType != null) {
            sb.append("pDNType=");
            sb.append(this.pDNType.toString());
            sb.append(", ");
        }

        if (this.servedPartyIPIPv4Address != null) {
            sb.append("servedPartyIPIPv4Address=");
            sb.append(this.servedPartyIPIPv4Address.toString());
            sb.append(", ");
        }

        if (this.apn != null) {
            sb.append("apn=");
            sb.append(this.apn.toString());
            sb.append(", ");
        }

        if (this.ePSQoSSubscribed != null) {
            sb.append("ePSQoSSubscribed=");
            sb.append(this.ePSQoSSubscribed.toString());
            sb.append(", ");
        }

        if (this.pdnGwIdentity != null) {
            sb.append("pdnGwIdentity=");
            sb.append(this.pdnGwIdentity.toString());
            sb.append(", ");
        }

        if (this.pdnGwAllocationType != null) {
            sb.append("pdnGwAllocationType=");
            sb.append(this.pdnGwAllocationType.getType());
            sb.append(", ");
        }

        if (this.vplmnAddressAllowed!=null) {
            sb.append("vplmnAddressAllowed, ");
        }

        if (this.chargingCharacteristics != null) {
            sb.append("chargingCharacteristics=");
            sb.append(this.chargingCharacteristics.toString());
            sb.append(", ");
        }

        if (this.ambr != null) {
            sb.append("ambr=");
            sb.append(this.ambr.toString());
            sb.append(", ");
        }

        if (this.specificAPNInfoList != null && this.specificAPNInfoList.getSpecificAPNInfo()!=null) {
            sb.append("specificAPNInfoList=[");
            boolean firstItem = true;
            for (SpecificAPNInfoImpl be : this.specificAPNInfoList.getSpecificAPNInfo()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.servedPartyIPIPv6Address != null) {
            sb.append("servedPartyIPIPv6Address=");
            sb.append(this.servedPartyIPIPv6Address.toString());
            sb.append(", ");
        }

        if (this.apnOiReplacement != null) {
            sb.append("apnOiReplacement=");
            sb.append(this.apnOiReplacement.toString());
            sb.append(", ");
        }

        if (this.siptoPermission != null) {
            sb.append("siptoPermission=");
            sb.append(this.siptoPermission.getType());
            sb.append(", ");
        }

        if (this.lipaPermission != null) {
            sb.append("lipaPermission=");
            sb.append(this.lipaPermission.getType());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
}
