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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedInfoWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class PromptAndCollectUserInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements
        PromptAndCollectUserInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = false,index = -1)
    private SendingLegIDWrapperImpl legID;

	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 2,constructed = false,index = -1)
    private ASNBoolean requestAnnouncementStartedNotification;

	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 3,constructed = false,index = -1)
    private ASNBoolean requestAnnouncementCompleteNotification;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private CollectedInfoWrapperImpl collectedInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNBoolean disconnectFromIPForbidden;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private InformationToSendWrapperImpl informationToSend;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    public PromptAndCollectUserInformationRequestImpl() {
    }

    public PromptAndCollectUserInformationRequestImpl(LegType legID, Boolean requestAnnouncementStartedNotification,
    		Boolean requestAnnouncementCompleteNotification, CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions) {
    	this(collectedInfo, disconnectFromIPForbidden, informationToSend, extensions);
    	
    	if(legID!=null) 
    		this.legID=new SendingLegIDWrapperImpl(new SendingLegIDImpl(legID));
    	
    	if(requestAnnouncementStartedNotification!=null) {
    		this.requestAnnouncementStartedNotification = new ASNBoolean();
    		this.requestAnnouncementStartedNotification.setValue(requestAnnouncementStartedNotification);
    	}
    	
    	if(requestAnnouncementCompleteNotification!=null) {
    		this.requestAnnouncementCompleteNotification = new ASNBoolean();
    		this.requestAnnouncementCompleteNotification.setValue(requestAnnouncementCompleteNotification);
    	}
    }
    
    public PromptAndCollectUserInformationRequestImpl(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions) {
    	
    	if(collectedInfo!=null)
    		this.collectedInfo = new CollectedInfoWrapperImpl(collectedInfo);
    	
    	if(disconnectFromIPForbidden!=null) {
    		this.disconnectFromIPForbidden = new ASNBoolean();
    		this.disconnectFromIPForbidden.setValue(disconnectFromIPForbidden);
    	}
    	
    	if(informationToSend!=null)
    		this.informationToSend = new InformationToSendWrapperImpl(informationToSend);
    	
        this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.promptAndCollectUserInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.promptAndCollectUserInformation;
    }

    @Override
    public LegType getLegID() {
    	if(legID==null || legID.getSendingLegID()==null || legID.getSendingLegID().getSendingSideID()==null)
    		return null;
    	
        return legID.getSendingLegID().getSendingSideID();
    }

    @Override
    public Boolean getRequestAnnouncementStartedNotification() {
    	if(requestAnnouncementStartedNotification==null)
    		return null;
    	
        return requestAnnouncementStartedNotification.getValue();
    }

    @Override
    public Boolean getRequestAnnouncementCompleteNotification() {
    	if(requestAnnouncementCompleteNotification==null)
    		return null;
    	
        return requestAnnouncementCompleteNotification.getValue();
    }

    @Override
    public CollectedInfo getCollectedInfo() {
    	if(collectedInfo==null)
    		return null;
    	
        return collectedInfo.getCollectedInfo();
    }

    @Override
    public Boolean getDisconnectFromIPForbidden() {
    	if(disconnectFromIPForbidden==null)
    		return null;
    	
        return disconnectFromIPForbidden.getValue();
    }

    @Override
    public InformationToSend getInformationToSend() {
    	if(informationToSend==null)
    		return null;
    	
        return informationToSend.getInformationToSend();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("PromptAndCollectUserInformationRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legID != null && this.legID.getSendingLegID()!=null && this.legID.getSendingLegID().getSendingSideID()!=null) {
            sb.append(", legID=");
            sb.append(this.legID.getSendingLegID().getSendingSideID());
        }
        if (this.requestAnnouncementStartedNotification != null && this.requestAnnouncementStartedNotification.getValue()) {
            sb.append(", requestAnnouncementStartedNotification=");
            sb.append(requestAnnouncementStartedNotification.getValue());
        }
        if (this.requestAnnouncementCompleteNotification != null && this.requestAnnouncementCompleteNotification.getValue()) {
            sb.append(", requestAnnouncementCompleteNotification=");
            sb.append(requestAnnouncementCompleteNotification.getValue());
        }
        if (this.collectedInfo != null && this.collectedInfo.getCollectedInfo()!=null) {
            sb.append(", collectedInfo=");
            sb.append(collectedInfo.getCollectedInfo());
        }
        if (this.disconnectFromIPForbidden != null && this.disconnectFromIPForbidden.getValue()!=null) {
            sb.append(", disconnectFromIPForbidden=");
            sb.append(disconnectFromIPForbidden.getValue());
        }
        if (this.informationToSend != null && this.informationToSend.getInformationToSend()!=null) {
            sb.append(", informationToSend=");
            sb.append(informationToSend.getInformationToSend());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
