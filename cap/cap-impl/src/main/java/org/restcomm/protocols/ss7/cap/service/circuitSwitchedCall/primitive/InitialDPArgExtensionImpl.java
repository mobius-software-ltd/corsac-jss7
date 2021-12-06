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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LowLayerCompatibility;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierInformation;
import org.restcomm.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.restcomm.protocols.ss7.inap.isup.HighLayerCompatibilityInapImpl;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.UUDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitialDPArgExtensionImpl implements InitialDPArgExtension {
	//CAP V2
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = NACarrierInformationImpl.class)
    private NACarrierInformation naCarrierInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gmscAddress;
    
    //CAP V4
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gmscAddress2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = CalledPartyNumberCapImpl.class)
    private CalledPartyNumberCap forwardingDestinationNumber;
    
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
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = HighLayerCompatibilityInapImpl.class)
    private HighLayerCompatibilityInap highLayerCompatibility2;
    
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
    public InitialDPArgExtensionImpl() {
    }
    
    public InitialDPArgExtensionImpl(NACarrierInformation naCarrierInformation, ISDNAddressString gmscAddress) {
    	this.naCarrierInformation = naCarrierInformation;
    	this.gmscAddress = gmscAddress;
    }

    public InitialDPArgExtensionImpl(ISDNAddressString gmscAddress, CalledPartyNumberCap forwardingDestinationNumber,
            MSClassmark2 msClassmark2, IMEI imei, SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities, BearerCapability bearerCapability2,
            ExtBasicServiceCode extBasicServiceCode2, HighLayerCompatibilityInap highLayerCompatibility2,
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

    public void patchVersion(int version) throws CAPParsingComponentException {
    	if(forwardingDestinationNumber==null)
    		return;
    	    	
    	if(version<3) {
    		try {
	    		gmscAddress=new ISDNAddressStringImpl();
	    		((ISDNAddressStringImpl)gmscAddress).decode(null, this, Unpooled.wrappedBuffer(forwardingDestinationNumber.getData()), null);
	    		forwardingDestinationNumber = null;
    		} 
    		catch(MAPParsingComponentException ex) {
    			throw new CAPParsingComponentException(ex.getMessage(), null);
    		}
    	}
    }
    
    public ISDNAddressString getGmscAddress() {
    	if(gmscAddress!=null)
    		return gmscAddress;
    	
    	return gmscAddress2;
    }

    public NACarrierInformation getNACarrierInformation() {
    	return naCarrierInformation;
    }
    
    public CalledPartyNumberCap getForwardingDestinationNumber() {
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

    public HighLayerCompatibilityInap getHighLayerCompatibility2() {
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

        if (this.naCarrierInformation != null) {
            sb.append(", naCarrierInformation=");
            sb.append(naCarrierInformation);
        }
        
        if (this.gmscAddress != null) {
            sb.append(", gmscAddress=");
            sb.append(gmscAddress);
        }

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
