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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

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
public class UpdateGprsLocationResponseImpl extends MobilityMessageImpl implements UpdateGprsLocationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString hlrNumber;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true, index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
	private ASNNull addCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull sgsnMmeSeparationSupported;

    public UpdateGprsLocationResponseImpl() {
        super();
    }

    public UpdateGprsLocationResponseImpl(ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer,
            boolean addCapability, boolean sgsnMmeSeparationSupported) {
        super();
        this.hlrNumber = hlrNumber;
        this.extensionContainer = extensionContainer;
        
        if(addCapability)
        	this.addCapability = new ASNNull();
        
        if(sgsnMmeSeparationSupported)
        	this.sgsnMmeSeparationSupported = new ASNNull();
    }

    @Override
    public ISDNAddressString getHlrNumber() {
        return hlrNumber;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public boolean getAddCapability() {
        return this.addCapability!=null;
    }

    @Override
    public boolean getSgsnMmeSeparationSupported() {
        return this.sgsnMmeSeparationSupported!=null;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.updateGprsLocation_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.updateGprsLocation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateGprsLocationResponse [");

        if (this.hlrNumber != null) {
            sb.append("hlrNumber=");
            sb.append(this.hlrNumber.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.addCapability!=null) {
            sb.append("addCapability, ");
        }

        if (this.sgsnMmeSeparationSupported!=null) {
            sb.append("sgsnMmeSeparationSupported, ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(hlrNumber==null)
			throw new ASNParsingComponentException("hlr number should be set for update gprs location response", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
