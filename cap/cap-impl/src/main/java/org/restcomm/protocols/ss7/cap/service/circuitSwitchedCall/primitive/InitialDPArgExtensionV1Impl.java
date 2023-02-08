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
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitialDPArgExtensionV1Impl implements InitialDPArgExtension {
	//CAP V2
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = NACarrierInformationImpl.class)
    private NACarrierInformation naCarrierInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gmscAddress;
    
    /**
     * This constructor is for deserializing purposes
     */
    public InitialDPArgExtensionV1Impl() {
    }
    
    public InitialDPArgExtensionV1Impl(NACarrierInformation naCarrierInformation, ISDNAddressString gmscAddress) {
    	this.naCarrierInformation = naCarrierInformation;
    	this.gmscAddress = gmscAddress;
    }
    
    public ISDNAddressString getGmscAddress() {
    	return gmscAddress;
    }

    public NACarrierInformation getNACarrierInformation() {
    	return naCarrierInformation;
    }
    
    public CalledPartyNumberIsup getForwardingDestinationNumber() {
        return null;
    }

    public MSClassmark2 getMSClassmark2() {
        return null;
    }

    public IMEI getIMEI() {
        return null;
    }

    public SupportedCamelPhases getSupportedCamelPhases() {
        return null;
    }

    public OfferedCamel4Functionalities getOfferedCamel4Functionalities() {
        return null;
    }

    public BearerCapability getBearerCapability2() {
    	return null;
    }

    public ExtBasicServiceCode getExtBasicServiceCode2() {
    	return null;
    }

    public HighLayerCompatibilityIsup getHighLayerCompatibility2() {
        return null;
    }

    public LowLayerCompatibility getLowLayerCompatibility() {
        return null;
    }

    public LowLayerCompatibility getLowLayerCompatibility2() {
        return null;
    }

    public boolean getEnhancedDialledServicesAllowed() {
        return null!=null;
    }

    public UUData getUUData() {
        return null;
    }

    public boolean getCollectInformationAllowed() {
        return false;
    }

    public boolean getReleaseCallArgExtensionAllowed() {
        return false;
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

        sb.append("]");

        return sb.toString();
    }
}
