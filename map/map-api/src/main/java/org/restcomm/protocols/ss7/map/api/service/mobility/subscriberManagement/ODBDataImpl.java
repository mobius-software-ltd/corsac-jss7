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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ODBDataImpl {
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=0)    
    private ODBGeneralDataImpl oDBGeneralData;
    
    private ODBHPLMNDataImpl odbHplmnData;
    private MAPExtensionContainerImpl extensionContainer;

    public ODBDataImpl() {
    }

    public ODBDataImpl(ODBGeneralDataImpl oDBGeneralData, ODBHPLMNDataImpl odbHplmnData, MAPExtensionContainerImpl extensionContainer) {
        this.oDBGeneralData = oDBGeneralData;
        this.odbHplmnData = odbHplmnData;
        this.extensionContainer = extensionContainer;
    }

    public ODBGeneralDataImpl getODBGeneralData() {
        return this.oDBGeneralData;
    }

    public ODBHPLMNDataImpl getOdbHplmnData() {
        return this.odbHplmnData;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
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

}
