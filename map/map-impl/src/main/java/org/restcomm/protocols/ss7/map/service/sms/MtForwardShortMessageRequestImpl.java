/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfoImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MtForwardShortMessageRequestImpl extends SmsMessageImpl implements MtForwardShortMessageRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private SM_RP_DAImpl sM_RP_DA;
	
	@ASNChoise
    private SM_RP_OAImpl sM_RP_OA;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private SmsSignalInfoImpl sM_RP_UI;
    
	private ASNNull moreMessagesToSend;
    private MAPExtensionContainerImpl extensionContainer;

    public MtForwardShortMessageRequestImpl() {
    }

    public MtForwardShortMessageRequestImpl(SM_RP_DAImpl sM_RP_DA, SM_RP_OAImpl sM_RP_OA, SmsSignalInfoImpl sM_RP_UI,
            boolean moreMessagesToSend, MAPExtensionContainerImpl extensionContainer) {
        this.sM_RP_DA = sM_RP_DA;
        this.sM_RP_OA = sM_RP_OA;
        this.sM_RP_UI = (SmsSignalInfoImpl) sM_RP_UI;
        
        if(moreMessagesToSend)
        	this.moreMessagesToSend = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.mtForwardSM_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.mt_forwardSM;
    }

    public SM_RP_DAImpl getSM_RP_DA() {
        return this.sM_RP_DA;
    }

    public SM_RP_OAImpl getSM_RP_OA() {
        return this.sM_RP_OA;
    }

    public SmsSignalInfoImpl getSM_RP_UI() {
        return this.sM_RP_UI;
    }

    public boolean getMoreMessagesToSend() {
        return this.moreMessagesToSend!=null;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MtForwardShortMessageRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.sM_RP_DA != null) {
            sb.append(", sm_RP_DA=");
            sb.append(this.sM_RP_DA.toString());
        }
        if (this.sM_RP_OA != null) {
            sb.append(", sm_RP_OA=");
            sb.append(this.sM_RP_OA.toString());
        }
        if (this.sM_RP_UI != null) {
            sb.append(", sm_RP_UI=[");
            sb.append(this.sM_RP_UI.toString());
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.moreMessagesToSend!=null) {
            sb.append(", moreMessagesToSend");
        }

        sb.append("]");

        return sb.toString();
    }
}
