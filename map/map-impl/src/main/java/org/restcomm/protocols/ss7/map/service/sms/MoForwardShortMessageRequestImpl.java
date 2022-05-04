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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MoForwardShortMessageRequestImpl extends SmsMessageImpl implements ForwardShortMessageRequest,MoForwardShortMessageRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise(defaultImplementation = SM_RP_DAImpl.class)
    private SM_RP_DA sM_RP_DA;
	
	@ASNChoise(defaultImplementation = SM_RP_OAImpl.class)
    private SM_RP_OA sM_RP_OA;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2, defaultImplementation = SmsSignalInfoImpl.class)
	private SmsSignalInfo sm_RP_UI;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = IMSIImpl.class)
	private IMSI imsi;

    private ASNNull moreMessagesToSend;

    public MoForwardShortMessageRequestImpl() {
    }

    public MoForwardShortMessageRequestImpl(SM_RP_DA sM_RP_DA, SM_RP_OA sM_RP_OA, SmsSignalInfo sm_RP_UI,
            MAPExtensionContainer extensionContainer, IMSI imsi) {
    	this.sM_RP_DA=sM_RP_DA;
        this.sM_RP_OA=sM_RP_OA;
        this.sm_RP_UI = (SmsSignalInfoImpl) sm_RP_UI;
        this.extensionContainer = extensionContainer;
        this.imsi = imsi;
    }
    
    public MoForwardShortMessageRequestImpl(SM_RP_DA sM_RP_DA, SM_RP_OA sM_RP_OA, SmsSignalInfo sM_RP_UI,
            boolean moreMessagesToSend) {
    	this.sM_RP_DA=sM_RP_DA;
        this.sM_RP_OA=sM_RP_OA;
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

    public SM_RP_DA getSM_RP_DA() {
        return this.sM_RP_DA;
    }

    public SM_RP_OA getSM_RP_OA() {
        return this.sM_RP_OA;
    }

    public SmsSignalInfo getSM_RP_UI() {
        return this.sm_RP_UI;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public IMSI getIMSI() {
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

        if (this.sM_RP_DA != null) {
            sb.append(", sm_RP_DA=");
            sb.append(this.sM_RP_DA.toString());
        }
        if (this.sM_RP_OA != null) {
            sb.append(", sm_RP_OA=");
            sb.append(this.sM_RP_OA.toString());
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(sM_RP_DA==null)
			throw new ASNParsingComponentException("SM RP DA should be set for mo forward short message request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(sM_RP_OA==null)
			throw new ASNParsingComponentException("SM RP OA should be set for mo forward short message request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(sm_RP_UI==null)
			throw new ASNParsingComponentException("SM RP UI should be set for mo forward short message request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
