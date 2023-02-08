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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
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
 * @author kiss.balazs@alerant.hu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class PlayAnnouncementRequestImpl extends CircuitSwitchedCallMessageImpl implements PlayAnnouncementRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private InformationToSendWrapperImpl informationToSend;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNBoolean disconnectFromIPForbidden;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNBoolean requestAnnouncementCompleteNotification;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
    private ASNInteger callSegmentID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNBoolean requestAnnouncementStartedNotification;

    public PlayAnnouncementRequestImpl() {
    }

    public PlayAnnouncementRequestImpl(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) {
    	
    	if(informationToSend!=null)
    		this.informationToSend = new InformationToSendWrapperImpl(informationToSend);
    	
    	if(disconnectFromIPForbidden!=null)
    		this.disconnectFromIPForbidden = new ASNBoolean(disconnectFromIPForbidden,"DisconnectFromIPForbidden",true,false);

    	if(requestAnnouncementCompleteNotification!=null)
    		this.requestAnnouncementCompleteNotification = new ASNBoolean(requestAnnouncementCompleteNotification,"RequestAnnouncementCompleteNotification",true,false);
    		
        this.extensions = extensions;
        
        if(callSegmentID!=null)
        	this.callSegmentID = new ASNInteger(callSegmentID,"CallSegmentID",0,127,false);
        
        if(requestAnnouncementStartedNotification!=null)
        	this.requestAnnouncementStartedNotification = new ASNBoolean(requestAnnouncementStartedNotification,"RequestAnnouncementStartedNotification",true,false);        	
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.playAnnouncement_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.playAnnouncement;
    }

    @Override
    public InformationToSend getInformationToSend() {
    	if(informationToSend==null)
    		return null;
    	
        return informationToSend.getInformationToSend();
    }

    @Override
    public Boolean getDisconnectFromIPForbidden() {
    	if(disconnectFromIPForbidden==null || disconnectFromIPForbidden.getValue()==null)
    		return true;
    	
        return disconnectFromIPForbidden.getValue();
    }

    @Override
    public Boolean getRequestAnnouncementCompleteNotification() {
    	if(requestAnnouncementCompleteNotification==null || requestAnnouncementCompleteNotification.getValue()==null)
    		return true;
    	
        return requestAnnouncementCompleteNotification.getValue();
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
        sb.append("PlayAnnouncementRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.informationToSend != null && this.informationToSend.getInformationToSend()!=null) {
            sb.append(", informationToSend=");
            sb.append(informationToSend.getInformationToSend().toString());
        }
        if (this.disconnectFromIPForbidden != null && this.disconnectFromIPForbidden.getValue()!=null) {
            sb.append(", disconnectFromIPForbidden=");
            sb.append(disconnectFromIPForbidden.getValue());
        }
        if (this.requestAnnouncementCompleteNotification != null && this.requestAnnouncementCompleteNotification.getValue()!=null) {
            sb.append(", requestAnnouncementCompleteNotification=");
            sb.append(requestAnnouncementCompleteNotification.getValue());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.callSegmentID != null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID);
        }
        if (this.requestAnnouncementStartedNotification != null && this.requestAnnouncementStartedNotification.getValue()!=null) {
            sb.append(", requestAnnouncementStartedNotification=");
            sb.append(requestAnnouncementStartedNotification.getValue());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(informationToSend==null)
			throw new ASNParsingComponentException("information to send should be set for play announcement request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
