/*
 * Mobius Software LTD
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class InformServiceCentreRequestImpl extends SmsMessageImpl implements InformServiceCentreRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString storedMSISDN;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=-1, defaultImplementation = MWStatusImpl.class)
	private MWStatus mwStatus;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    private ASNInteger absentSubscriberDiagnosticSM;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger additionalAbsentSubscriberDiagnosticSM;

    public InformServiceCentreRequestImpl() {
    }

    public InformServiceCentreRequestImpl(ISDNAddressString storedMSISDN, MWStatus mwStatus,
            MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) {
        this.storedMSISDN = storedMSISDN;
        this.mwStatus = mwStatus;
        this.extensionContainer = extensionContainer;
        
        if(absentSubscriberDiagnosticSM!=null)
        	this.absentSubscriberDiagnosticSM = new ASNInteger(absentSubscriberDiagnosticSM,"AbsentSubscriberDiagnosticSM",0,12,false);
        	
        if(additionalAbsentSubscriberDiagnosticSM!=null)
        	this.additionalAbsentSubscriberDiagnosticSM = new ASNInteger(additionalAbsentSubscriberDiagnosticSM,"AdditionalAbsentSubscriberDiagnosticSM",0,12,false);
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.InformServiceCentre_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.informServiceCentre;
    }

    public ISDNAddressString getStoredMSISDN() {
        return this.storedMSISDN;
    }

    public MWStatus getMwStatus() {
        return this.mwStatus;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public Integer getAbsentSubscriberDiagnosticSM() {
    	if(this.absentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.absentSubscriberDiagnosticSM.getIntValue();
    }

    public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
    	if(this.additionalAbsentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.additionalAbsentSubscriberDiagnosticSM.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InformServiceCentreRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.storedMSISDN != null) {
            sb.append(", storedMSISDN=");
            sb.append(this.storedMSISDN.toString());
        }
        if (this.mwStatus != null) {
            sb.append(", mwStatus=");
            sb.append(this.mwStatus.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.absentSubscriberDiagnosticSM != null) {
            sb.append(", absentSubscriberDiagnosticSM=");
            sb.append(this.absentSubscriberDiagnosticSM.getValue());
        }
        if (this.additionalAbsentSubscriberDiagnosticSM != null) {
            sb.append(", additionalAbsentSubscriberDiagnosticSM=");
            sb.append(this.additionalAbsentSubscriberDiagnosticSM.getValue());
        }

        sb.append("]");

        return sb.toString();
    }

}
