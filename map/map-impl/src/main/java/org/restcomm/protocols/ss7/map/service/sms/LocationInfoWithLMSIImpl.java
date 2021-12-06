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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,lengthIndefinite=false)
public class LocationInfoWithLMSIImpl implements LocationInfoWithLMSI {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=0, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString networkNodeNumber;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
	private LMSI lmsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNNull gprsNodeIndicator;
        
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private AdditionalNumberWrapperImpl additionalNumber;

    public LocationInfoWithLMSIImpl() {
    }

    public LocationInfoWithLMSIImpl(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, boolean gprsNodeIndicator,
            AdditionalNumber additionalNumber) {
    	this.networkNodeNumber = networkNodeNumber;
        this.lmsi = lmsi;
        this.extensionContainer = extensionContainer;
        
        if(gprsNodeIndicator)
        	this.gprsNodeIndicator = new ASNNull();
        
        if(additionalNumber!=null)
        	this.additionalNumber = new AdditionalNumberWrapperImpl(additionalNumber);
    }

    public ISDNAddressString getNetworkNodeNumber() {
        return this.networkNodeNumber;
    }

    public LMSI getLMSI() {
        return this.lmsi;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public boolean getGprsNodeIndicator() {
        return gprsNodeIndicator!=null;
    }

    public AdditionalNumber getAdditionalNumber() {
    	if(this.additionalNumber==null)
    		return null;
    	
        return this.additionalNumber.getAdditionalNumber();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInfoWithLMSI [");

        if (this.networkNodeNumber != null) {
            sb.append("networkNodeNumber=");
            sb.append(this.networkNodeNumber.toString());
        }
        if (this.lmsi != null) {
            sb.append(", lmsi=");
            sb.append(this.lmsi.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.gprsNodeIndicator!=null) {
            sb.append(", gprsNodeIndicator");
        }
        if (this.additionalNumber != null) {
            sb.append(", additionalNumber=");
            sb.append(this.additionalNumber.getAdditionalNumber());
        }

        sb.append("]");

        return sb.toString();
    }
}
