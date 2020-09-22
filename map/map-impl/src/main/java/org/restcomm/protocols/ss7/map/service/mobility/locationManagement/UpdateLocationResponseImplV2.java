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
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UpdateLocationResponseImplV2 extends MobilityMessageImpl implements UpdateLocationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ISDNAddressStringImpl hlrNumber;
	
    private MAPExtensionContainerImpl extensionContainer;
    private ASNNull addCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull pagingAreaCapability;
    private long mapProtocolVersion;

    public UpdateLocationResponseImplV2(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public UpdateLocationResponseImplV2(long mapProtocolVersion, ISDNAddressStringImpl hlrNumber,
            MAPExtensionContainerImpl extensionContainer, boolean addCapability, boolean pagingAreaCapability) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.hlrNumber = hlrNumber;
        this.extensionContainer = extensionContainer;
        
        if(addCapability)
        	this.addCapability = new ASNNull();
        
        if(pagingAreaCapability)
        	this.pagingAreaCapability = new ASNNull();
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.updateLocation_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.updateLocation;
    }

    public ISDNAddressStringImpl getHlrNumber() {
        return hlrNumber;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public boolean getAddCapability() {
        return addCapability!=null;
    }

    public boolean getPagingAreaCapability() {
        return pagingAreaCapability!=null;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateLocationResponse [");

        if (this.hlrNumber != null) {
            sb.append("hlrNumber=");
            sb.append(hlrNumber.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.addCapability!=null) {
            sb.append("addCapability, ");
        }
        if (this.pagingAreaCapability!=null) {
            sb.append("pagingAreaCapability, ");
        }
        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}
