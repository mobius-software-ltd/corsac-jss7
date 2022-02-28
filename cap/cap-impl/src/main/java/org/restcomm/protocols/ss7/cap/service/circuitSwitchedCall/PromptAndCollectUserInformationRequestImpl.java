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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedInfoWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class PromptAndCollectUserInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements
        PromptAndCollectUserInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private CollectedInfoWrapperImpl collectedInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNBoolean disconnectFromIPForbidden;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private InformationToSendWrapperImpl informationToSend;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private ASNInteger callSegmentID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNBoolean requestAnnouncementStartedNotification;

    public PromptAndCollectUserInformationRequestImpl() {
    }

    public PromptAndCollectUserInformationRequestImpl(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) {
    	
    	if(collectedInfo!=null)
    		this.collectedInfo = new CollectedInfoWrapperImpl(collectedInfo);
    	
    	if(disconnectFromIPForbidden!=null)
    		this.disconnectFromIPForbidden = new ASNBoolean(disconnectFromIPForbidden,"DisconnectFromIPForbidden",true,false);
    		
    	if(informationToSend!=null)
    		this.informationToSend = new InformationToSendWrapperImpl(informationToSend);
    	
        this.extensions = extensions;
        
        if(callSegmentID!=null)
        	this.callSegmentID = new ASNInteger(callSegmentID,"CallSegmentID",0,127,false);
        	
        if(requestAnnouncementStartedNotification!=null)
        	this.requestAnnouncementStartedNotification = new ASNBoolean(requestAnnouncementStartedNotification,"RequestAnnouncementStartedNotification",true,false);        	
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.promptAndCollectUserInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.promptAndCollectUserInformation;
    }

    @Override
    public CollectedInfo getCollectedInfo() {
    	if(collectedInfo==null)
    		return null;
    	
        return collectedInfo.getCollectedInfo();
    }

    @Override
    public Boolean getDisconnectFromIPForbidden() {
    	if(disconnectFromIPForbidden==null || disconnectFromIPForbidden.getValue()==null)
    		return true;
    	
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
    public Integer getCallSegmentID() {
    	if(callSegmentID==null)
    		return null;
    	
        return callSegmentID.getIntValue();
    }

    @Override
    public Boolean getRequestAnnouncementStartedNotification() {
    	if(requestAnnouncementStartedNotification==null || requestAnnouncementStartedNotification.getValue()==null)
    		return false;
    	
        return requestAnnouncementStartedNotification.getValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("PromptAndCollectUserInformationRequestIndication [");
        this.addInvokeIdInfo(sb);

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
        if (this.callSegmentID != null && this.callSegmentID.getValue()!=null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID.getValue());
        }
        if (this.requestAnnouncementStartedNotification != null && this.requestAnnouncementStartedNotification.getValue()) {
            sb.append(", requestAnnouncementStartedNotification=");
            sb.append(requestAnnouncementStartedNotification.getValue());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(collectedInfo==null)
			throw new ASNParsingComponentException("collected info should be set for prompt and collect request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
