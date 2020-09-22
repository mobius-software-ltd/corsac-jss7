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
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
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
public class MoForwardShortMessageRequestImpl extends SmsMessageImpl implements ForwardShortMessageRequest,MoForwardShortMessageRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private SM_RP_DAImpl sm_RP_DA;
	
	@ASNChoise
    private SM_RP_OAImpl sm_RP_OA;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2)
	private SmsSignalInfoImpl sm_RP_UI;
    
	private MAPExtensionContainerImpl extensionContainer;
    private IMSIImpl imsi;

    private ASNNull moreMessagesToSend;

    public MoForwardShortMessageRequestImpl() {
    }

    public MoForwardShortMessageRequestImpl(SM_RP_DAImpl sm_RP_DA, SM_RP_OAImpl sm_RP_OA, SmsSignalInfoImpl sm_RP_UI,
            MAPExtensionContainerImpl extensionContainer, IMSIImpl imsi) {
        this.sm_RP_DA = sm_RP_DA;
        this.sm_RP_OA = sm_RP_OA;
        this.sm_RP_UI = (SmsSignalInfoImpl) sm_RP_UI;
        this.extensionContainer = extensionContainer;
        this.imsi = imsi;
    }
    
    public MoForwardShortMessageRequestImpl(SM_RP_DAImpl sM_RP_DA, SM_RP_OAImpl sM_RP_OA, SmsSignalInfoImpl sM_RP_UI,
            boolean moreMessagesToSend) {
        this.sm_RP_DA = sM_RP_DA;
        this.sm_RP_OA = sM_RP_OA;
        this.sm_RP_UI = (SmsSignalInfoImpl) sM_RP_UI;
        
        if(moreMessagesToSend)
        	this.moreMessagesToSend = new ASNNull();
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.moForwardSM_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.mo_forwardSM;
    }

    public SM_RP_DAImpl getSM_RP_DA() {
        return this.sm_RP_DA;
    }

    public SM_RP_OAImpl getSM_RP_OA() {
        return this.sm_RP_OA;
    }

    public SmsSignalInfoImpl getSM_RP_UI() {
        return this.sm_RP_UI;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public IMSIImpl getIMSI() {
        return this.imsi;
    }

    public boolean getMoreMessagesToSend() {
        return this.moreMessagesToSend!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MoForwardShortMessageRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.sm_RP_DA != null) {
            sb.append(", sm_RP_DA=");
            sb.append(this.sm_RP_DA.toString());
        }
        if (this.sm_RP_OA != null) {
            sb.append(", sm_RP_OA=");
            sb.append(this.sm_RP_OA.toString());
        }
        if (this.sm_RP_UI != null) {
            sb.append(", sm_RP_UI=[");
            sb.append(this.sm_RP_UI.toString());
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi.toString());
        }

        if (this.moreMessagesToSend!=null) {
            sb.append(", moreMessagesToSend");
        }
        
        sb.append("]");

        return sb.toString();
    }
}
