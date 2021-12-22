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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfigurationProfile;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;

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
public class EPSSubscriptionDataImpl implements EPSSubscriptionData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = APNOIReplacementImpl.class)
    private APNOIReplacement apnOiReplacement;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger rfspId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = AMBRImpl.class)
    private AMBR ambr;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1,defaultImplementation = APNConfigurationProfileImpl.class)
    private APNConfigurationProfile apnConfigurationProfile;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString stnSr;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull mpsCSPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull mpsEPSPriority;

    public EPSSubscriptionDataImpl() {
    }

    public EPSSubscriptionDataImpl(APNOIReplacement apnOiReplacement, Integer rfspId, AMBR ambr,
            APNConfigurationProfile apnConfigurationProfile, ISDNAddressString stnSr, MAPExtensionContainer extensionContainer,
            boolean mpsCSPriority, boolean mpsEPSPriority) {
        this.apnOiReplacement = apnOiReplacement;
        
        if(rfspId!=null) {
        	this.rfspId = new ASNInteger();
        	this.rfspId.setValue(rfspId.longValue());
        }
        
        this.ambr = ambr;
        this.apnConfigurationProfile = apnConfigurationProfile;
        this.stnSr = stnSr;
        this.extensionContainer = extensionContainer;
        
        if(mpsCSPriority)
        	this.mpsCSPriority = new ASNNull();
        
        if(mpsEPSPriority)
        	this.mpsEPSPriority = new ASNNull();
    }

    public APNOIReplacement getApnOiReplacement() {
        return this.apnOiReplacement;
    }

    public Integer getRfspId() {
    	if(this.rfspId==null)
    		return null;
    	
        return this.rfspId.getValue().intValue();
    }

    public AMBR getAmbr() {
        return this.ambr;
    }

    public APNConfigurationProfile getAPNConfigurationProfile() {
        return this.apnConfigurationProfile;
    }

    public ISDNAddressString getStnSr() {
        return this.stnSr;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public boolean getMpsCSPriority() {
        return this.mpsCSPriority!=null;
    }

    public boolean getMpsEPSPriority() {
        return this.mpsEPSPriority!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EPSSubscriptionData [");

        if (this.apnOiReplacement != null) {
            sb.append("apnOiReplacement=");
            sb.append(this.apnOiReplacement.toString());
            sb.append(", ");
        }

        if (this.rfspId != null) {
            sb.append("rfspId=");
            sb.append(this.rfspId.getValue());
            sb.append(", ");
        }
        if (this.ambr != null) {
            sb.append("ambr=");
            sb.append(this.ambr.toString());
            sb.append(", ");
        }

        if (this.apnConfigurationProfile != null) {
            sb.append("apnConfigurationProfile=");
            sb.append(this.apnConfigurationProfile.toString());
            sb.append(", ");
        }

        if (this.stnSr != null) {
            sb.append("stnSr=");
            sb.append(this.stnSr.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.mpsCSPriority!=null) {
            sb.append("mpsCSPriority, ");
        }

        if (this.mpsEPSPriority!=null) {
            sb.append("mpsEPSPriority ");
        }

        sb.append("]");

        return sb.toString();
    }
}
