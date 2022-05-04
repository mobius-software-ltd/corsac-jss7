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
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ODBDataImpl implements ODBData {
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=0,defaultImplementation = ODBGeneralDataImpl.class)    
    private ODBGeneralData oDBGeneralData;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=-1,defaultImplementation = ODBHPLMNDataImpl.class)
    private ODBHPLMNData odbHplmnData;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public ODBDataImpl() {
    }

    public ODBDataImpl(ODBGeneralData oDBGeneralData, ODBHPLMNData odbHplmnData, MAPExtensionContainer extensionContainer) {
        this.oDBGeneralData = oDBGeneralData;
        this.odbHplmnData = odbHplmnData;
        this.extensionContainer = extensionContainer;
    }

    public ODBGeneralData getODBGeneralData() {
        return this.oDBGeneralData;
    }

    public ODBHPLMNData getOdbHplmnData() {
        return this.odbHplmnData;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ODBData [");

        if (this.oDBGeneralData != null) {
            sb.append("oDBGeneralData=");
            sb.append(this.oDBGeneralData.toString());
            sb.append(", ");
        }

        if (this.odbHplmnData != null) {
            sb.append("odbHplmnData=");
            sb.append(this.odbHplmnData.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(oDBGeneralData==null)
			throw new ASNParsingComponentException("odb general data should be set for odb data", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
