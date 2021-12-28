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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNNumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CollectInformationRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;

/**
*
 * @author yulian.oifa
*
*/
public class CollectInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements CollectInformationRequest {
	private static final long serialVersionUID = 1L;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
	private AlertingPatternWrapperImpl alertingPattern;
	    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
	private ASNNumberingPlan numberingPlan;
	    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = OriginalCalledPartyIDIsupImpl.class)
	private OriginalCalledPartyIDIsup originalCalledPartyID;
	    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = LocationNumberIsupImpl.class)
	private LocationNumberIsup travellingClassMark;
	    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1,defaultImplementation = CallingPartyNumberIsupImpl.class)
	private CallingPartyNumberIsup callingPartyNumber;
	    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1,defaultImplementation = CalledPartyNumberIsupImpl.class)
	private CalledPartyNumberIsup dialedDigits;
	    
	public CollectInformationRequestImpl() {
	}

	public CollectInformationRequestImpl(AlertingPattern alertingPattern, NumberingPlan numberingPlan, OriginalCalledPartyIDIsup originalCalledPartyID,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, CallingPartyNumberIsup callingPartyNumber, CalledPartyNumberIsup dialedDigits) {
	    	
		if(alertingPattern!=null)
			this.alertingPattern=new AlertingPatternWrapperImpl(alertingPattern);			
		
        if(numberingPlan!=null) {
	    	this.numberingPlan = new ASNNumberingPlan();
	    	this.numberingPlan.setType(numberingPlan);
	    }
        
	    this.originalCalledPartyID = originalCalledPartyID;
	    this.travellingClassMark = travellingClassMark;
	    this.extensions = extensions;
	    this.callingPartyNumber = callingPartyNumber;
	    this.dialedDigits = dialedDigits;
	}
	    
	@Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.collectInformationRequest_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.collectInformation;
    }

    public AlertingPattern getAlertingPattern() {
    	if(alertingPattern==null)
    		return null;
    	
		return alertingPattern.getAlertingPattern();
	}

	public NumberingPlan getNumberingPlan() {
		if(numberingPlan==null)
			return null;
		
		return numberingPlan.getType();
	}

	public OriginalCalledPartyIDIsup getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

	public LocationNumberIsup getTravellingClassMark() {
		return travellingClassMark;
	}

	public CAPINAPExtensions getExtensions() {
		return extensions;
	}

	public CallingPartyNumberIsup getCallingPartyNumber() {
		return callingPartyNumber;
	}

	public CalledPartyNumberIsup getDialedDigits() {
		return dialedDigits;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CollectInformationRequest [");
        this.addInvokeIdInfo(sb);

        if (this.alertingPattern!=null && this.alertingPattern.getAlertingPattern()!=null) {
            sb.append(", alertingPattern=");
            sb.append(this.alertingPattern.getAlertingPattern());
        }
        
        if (this.numberingPlan != null && this.numberingPlan.getType()!=null) {
            sb.append(", numberingPlan=");
            sb.append(numberingPlan.getType());
        }
        
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID);
        }
        
        if (this.travellingClassMark != null) {
            sb.append(", travellingClassMark=");
            sb.append(travellingClassMark);
        }
        
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions);
        }
        
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber);
        }
        
        if (this.dialedDigits != null) {
            sb.append(", dialedDigits=");
            sb.append(dialedDigits);
        }
        
        sb.append("]");

        return sb.toString();
    }
}
