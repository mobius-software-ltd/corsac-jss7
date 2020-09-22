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
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

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
public class PurgeMSResponseImpl extends MobilityMessageImpl implements PurgeMSResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull freezeTMSI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull freezePTMSI;
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull freezeMTMSI;

    public PurgeMSResponseImpl() {
        super();
    }

    public PurgeMSResponseImpl(boolean freezeTMSI, boolean freezePTMSI, MAPExtensionContainerImpl extensionContainer,
            boolean freezeMTMSI) {
        super();
        
        if(freezeTMSI)
        	this.freezeTMSI = new ASNNull();
        
        if(freezePTMSI)
        	this.freezePTMSI = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        
        if(freezeMTMSI)
        	this.freezeMTMSI = new ASNNull();
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.purgeMS_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.purgeMS;
    }

    @Override
    public boolean getFreezeTMSI() {
        return this.freezeTMSI!=null;
    }

    @Override
    public boolean getFreezePTMSI() {
        return this.freezePTMSI!=null;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public boolean getFreezeMTMSI() {
        return this.freezeMTMSI!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PurgeMSResponse [");

        if (this.freezeTMSI!=null) {
            sb.append("freezeTMSI, ");
        }

        if (this.freezePTMSI!=null) {
            sb.append("freezePTMSI, ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        if (this.freezeMTMSI!=null) {
            sb.append("freezeMTMSI ");
        }

        sb.append("]");

        return sb.toString();
    }

}
