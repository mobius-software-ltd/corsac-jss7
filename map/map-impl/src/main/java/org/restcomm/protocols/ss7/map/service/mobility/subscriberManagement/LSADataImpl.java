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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAAttributes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LSADataImpl implements LSAData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0,defaultImplementation = LSAIdentityImpl.class)
    private LSAIdentity lsaIdentity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1,defaultImplementation = LSAAttributesImpl.class)
    private LSAAttributes lsaAttributes;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull lsaActiveModeIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public LSADataImpl() {
    }

    public LSADataImpl(LSAIdentity lsaIdentity, LSAAttributes lsaAttributes, boolean lsaActiveModeIndicator,
            MAPExtensionContainer extensionContainer) {
        this.lsaIdentity = lsaIdentity;
        this.lsaAttributes = lsaAttributes;
        
        if(lsaActiveModeIndicator)
        	this.lsaActiveModeIndicator = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public LSAIdentity getLSAIdentity() {
        return this.lsaIdentity;
    }

    public LSAAttributes getLSAAttributes() {
        return this.lsaAttributes;
    }

    public boolean getLsaActiveModeIndicator() {
        return this.lsaActiveModeIndicator!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LSAData [");

        if (this.lsaIdentity != null) {
            sb.append("lsaIdentity=");
            sb.append(this.lsaIdentity.toString());
            sb.append(", ");
        }

        if (this.lsaAttributes != null) {
            sb.append("lsaAttributes=");
            sb.append(this.lsaAttributes.toString());
            sb.append(", ");
        }

        if (this.lsaActiveModeIndicator!=null) {
            sb.append("lsaActiveModeIndicator, ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());

        }

        sb.append("]");

        return sb.toString();
    }
}
