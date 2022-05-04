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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAAttributes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(lsaIdentity==null)
			throw new ASNParsingComponentException("lsa identity should be set for lsa data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(lsaAttributes==null)
			throw new ASNParsingComponentException("lsa attributes should be set for lsa data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
