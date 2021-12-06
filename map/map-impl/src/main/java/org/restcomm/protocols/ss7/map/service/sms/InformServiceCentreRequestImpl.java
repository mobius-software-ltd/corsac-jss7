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
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
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
        
        if(absentSubscriberDiagnosticSM!=null) {
        	this.absentSubscriberDiagnosticSM = new ASNInteger();
        	this.absentSubscriberDiagnosticSM.setValue(absentSubscriberDiagnosticSM.longValue());
        }
        
        if(additionalAbsentSubscriberDiagnosticSM!=null) {
        	this.additionalAbsentSubscriberDiagnosticSM = new ASNInteger();
        	this.additionalAbsentSubscriberDiagnosticSM.setValue(additionalAbsentSubscriberDiagnosticSM.longValue());
        }
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
    	
        return this.absentSubscriberDiagnosticSM.getValue().intValue();
    }

    public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
    	if(this.additionalAbsentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.additionalAbsentSubscriberDiagnosticSM.getValue().intValue();
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
