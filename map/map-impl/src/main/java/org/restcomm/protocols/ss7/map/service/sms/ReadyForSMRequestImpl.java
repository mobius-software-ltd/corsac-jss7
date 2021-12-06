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

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

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
public class ReadyForSMRequestImpl extends SmsMessageImpl implements ReadyForSMRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
	
    private ASNAlertReason alertReason;
    private ASNNull alertReasonIndicator;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull additionalAlertReasonIndicator;

    public ReadyForSMRequestImpl() {
    }

    public ReadyForSMRequestImpl(IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator, MAPExtensionContainer extensionContainer,
            boolean additionalAlertReasonIndicator) {
        this.imsi = imsi;
        
        if(alertReason!=null) {
        	this.alertReason = new ASNAlertReason();
        	this.alertReason.setType(alertReason);
        }
        
        if(alertReasonIndicator)
        	this.alertReasonIndicator = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        
        if(additionalAlertReasonIndicator)
        	this.additionalAlertReasonIndicator = new ASNNull();
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.readyForSM_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.readyForSM;
    }

    @Override
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public AlertReason getAlertReason() {
    	if(alertReason==null)
    		return null;
    	
        return alertReason.getType();
    }

    @Override
    public boolean getAlertReasonIndicator() {
        return alertReasonIndicator!=null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public boolean getAdditionalAlertReasonIndicator() {
        return additionalAlertReasonIndicator!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReadyForSMRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }
        if (this.alertReason != null) {
            sb.append("alertReason");
            sb.append(alertReason.getType());
            sb.append(", ");
        }
        if (this.alertReasonIndicator!=null) {
            sb.append("alertReasonIndicator, ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }
        if (this.additionalAlertReasonIndicator!=null) {
            sb.append("additionalAlertReasonIndicator, ");
        }

        sb.append("]");

        return sb.toString();
    }

}
