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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.MessageImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ActivateTraceModeResponseImpl extends MessageImpl implements ActivateTraceModeResponse_Oam,ActivateTraceModeResponse_Mobility {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull traceSupportIndicator;

    public ActivateTraceModeResponseImpl() {
    }

    public ActivateTraceModeResponseImpl(MAPExtensionContainer extensionContainer, boolean traceSupportIndicator) {

        this.extensionContainer = extensionContainer;
        
        if(traceSupportIndicator)
        	this.traceSupportIndicator = new ASNNull();
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.activateTraceMode_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.activateTraceMode;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public MAPDialogOam getOamMAPDialog() {
        MAPDialog mapDialog = super.getMAPDialog();
        if(mapDialog instanceof MAPDialogOam)
        	return (MAPDialogOam) mapDialog;
        
        return null;
    }

    public MAPDialogMobility getMAPDialog() {
        MAPDialog mapDialog = super.getMAPDialog();
        return (MAPDialogMobility) mapDialog;
    }

    @Override
    public boolean getTraceSupportIndicator() {
        return traceSupportIndicator!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActivateTraceModeResponse [");

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }
        if (this.traceSupportIndicator!=null) {
            sb.append("traceSupportIndicator, ");
        }

        sb.append("]");

        return sb.toString();
    }

}
