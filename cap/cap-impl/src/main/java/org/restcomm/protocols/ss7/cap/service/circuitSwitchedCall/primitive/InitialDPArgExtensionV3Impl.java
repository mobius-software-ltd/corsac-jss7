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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierInformation;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUData;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.LowLayerCompatibility;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.callhandling.UUDataImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.LowLayerCompatibilityImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitialDPArgExtensionV3Impl implements InitialDPArgExtension {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gmscAddress2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup forwardingDestinationNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = MSClassmark2Impl.class)
    private MSClassmark2 msClassmark2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = IMEIImpl.class)
    private IMEI imei;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = SupportedCamelPhasesImpl.class)
    private SupportedCamelPhases supportedCamelPhases;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1, defaultImplementation = OfferedCamel4FunctionalitiesImpl.class)
    private OfferedCamel4Functionalities offeredCamel4Functionalities;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1)
    private BearerCapabilityWrapperImpl bearerCapability2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1)
    private ExtBasicServiceCodeWrapperImpl extBasicServiceCode2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = HighLayerCompatibilityIsupImpl.class)
    private HighLayerCompatibilityIsup highLayerCompatibility2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1, defaultImplementation = LowLayerCompatibilityImpl.class)
    private LowLayerCompatibility lowLayerCompatibility;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1, defaultImplementation = LowLayerCompatibilityImpl.class)
    private LowLayerCompatibility lowLayerCompatibility2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1)
    private ASNNull enhancedDialledServicesAllowed;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = true,index = -1, defaultImplementation = UUDataImpl.class)
    private UUData uuData;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false,index = -1)
    private ASNNull collectInformationAllowed;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false,index = -1)
    private ASNNull releaseCallArgExtensionAllowed;

    /**
     * This constructor is for deserializing purposes
     */
    public InitialDPArgExtensionV3Impl() {
    }
    
    public InitialDPArgExtensionV3Impl(ISDNAddressString gmscAddress, CalledPartyNumberIsup forwardingDestinationNumber,
            MSClassmark2 msClassmark2, IMEI imei, SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities, BearerCapability bearerCapability2,
            ExtBasicServiceCode extBasicServiceCode2, HighLayerCompatibilityIsup highLayerCompatibility2,
            LowLayerCompatibility lowLayerCompatibility, LowLayerCompatibility lowLayerCompatibility2,
            boolean enhancedDialledServicesAllowed, UUData uuData, boolean collectInformationAllowed,
            boolean releaseCallArgExtensionAllowed) {
        this.gmscAddress2 = gmscAddress;
        this.forwardingDestinationNumber = forwardingDestinationNumber;
        this.msClassmark2 = msClassmark2;
        this.imei = imei;
        this.supportedCamelPhases = supportedCamelPhases;
        this.offeredCamel4Functionalities = offeredCamel4Functionalities;
        
        if(bearerCapability2!=null)
        	this.bearerCapability2 = new BearerCapabilityWrapperImpl(bearerCapability2);
        
        if(extBasicServiceCode2!=null)
        	this.extBasicServiceCode2 = new ExtBasicServiceCodeWrapperImpl(extBasicServiceCode2);
        
        this.highLayerCompatibility2 = highLayerCompatibility2;
        this.lowLayerCompatibility = lowLayerCompatibility;
        this.lowLayerCompatibility2 = lowLayerCompatibility2;
        
        if(enhancedDialledServicesAllowed)
        	this.enhancedDialledServicesAllowed = new ASNNull();
        this.uuData = uuData;
        
        if(collectInformationAllowed)
        	this.collectInformationAllowed = new ASNNull();
        
        if(releaseCallArgExtensionAllowed)
        	this.releaseCallArgExtensionAllowed = new ASNNull();        
    }
    
    public ISDNAddressString getGmscAddress() {
    	return gmscAddress2;
    }

    public NACarrierInformation getNACarrierInformation() {
    	return null;
    }
    
    public CalledPartyNumberIsup getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    public MSClassmark2 getMSClassmark2() {
        return msClassmark2;
    }

    public IMEI getIMEI() {
        return imei;
    }

    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    public OfferedCamel4Functionalities getOfferedCamel4Functionalities() {
        return offeredCamel4Functionalities;
    }

    public BearerCapability getBearerCapability2() {
    	if(bearerCapability2==null)
    		return null;
    	
        return bearerCapability2.getBearerCapability();
    }

    public ExtBasicServiceCode getExtBasicServiceCode2() {
    	if(extBasicServiceCode2==null)
    		return null;
    	
        return extBasicServiceCode2.getExtBasicServiceCode();
    }

    public HighLayerCompatibilityIsup getHighLayerCompatibility2() {
        return highLayerCompatibility2;
    }

    public LowLayerCompatibility getLowLayerCompatibility() {
        return lowLayerCompatibility;
    }

    public LowLayerCompatibility getLowLayerCompatibility2() {
        return lowLayerCompatibility2;
    }

    public boolean getEnhancedDialledServicesAllowed() {
        return enhancedDialledServicesAllowed!=null;
    }

    public UUData getUUData() {
        return uuData;
    }

    public boolean getCollectInformationAllowed() {
        return collectInformationAllowed!=null;
    }

    public boolean getReleaseCallArgExtensionAllowed() {
        return releaseCallArgExtensionAllowed!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("InitialDPArgExtension [");

        if (this.gmscAddress2 != null) {
            sb.append(", gmscAddress=");
            sb.append(gmscAddress2);
        }
        
        if (this.forwardingDestinationNumber != null) {
            sb.append(", forwardingDestinationNumber=");
            sb.append(forwardingDestinationNumber);
        }
        if (this.msClassmark2 != null) {
            sb.append(", msClassmark2=");
            sb.append(msClassmark2.toString());
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(imei.toString());
        }
        if (this.supportedCamelPhases != null) {
            sb.append(", supportedCamelPhases=");
            sb.append(supportedCamelPhases.toString());
        }
        if (this.offeredCamel4Functionalities != null) {
            sb.append(", offeredCamel4Functionalities=");
            sb.append(offeredCamel4Functionalities.toString());
        }
        if (this.bearerCapability2 != null) {
            sb.append(", bearerCapability2=");
            sb.append(bearerCapability2.toString());
        }
        if (this.extBasicServiceCode2 != null) {
            sb.append(", extBasicServiceCode2=");
            sb.append(extBasicServiceCode2.toString());
        }
        if (this.highLayerCompatibility2 != null) {
            sb.append(", highLayerCompatibility2=");
            sb.append(highLayerCompatibility2.toString());
        }
        if (this.lowLayerCompatibility != null) {
            sb.append(", lowLayerCompatibility=");
            sb.append(lowLayerCompatibility.toString());
        }
        if (this.lowLayerCompatibility2 != null) {
            sb.append(", lowLayerCompatibility2=");
            sb.append(lowLayerCompatibility2.toString());
        }
        if (this.enhancedDialledServicesAllowed!=null) {
            sb.append(", enhancedDialledServicesAllowed");
        }
        if (this.uuData != null) {
            sb.append(", uuData=");
            sb.append(uuData.toString());
        }
        if (this.collectInformationAllowed!=null) {
            sb.append(", collectInformationAllowed");
        }
        if (this.releaseCallArgExtensionAllowed!=null) {
            sb.append(", releaseCallArgExtensionAllowed");
        }

        sb.append("]");

        return sb.toString();
    }
}
