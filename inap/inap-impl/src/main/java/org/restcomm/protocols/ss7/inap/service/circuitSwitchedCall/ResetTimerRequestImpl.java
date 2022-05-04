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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNTimerID;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ResetTimerRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ResetTimerRequestImpl extends CircuitSwitchedCallMessageImpl implements ResetTimerRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNTimerID timerID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger timerValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public ResetTimerRequestImpl() {
    }

    public ResetTimerRequestImpl(TimerID timerID, int timerValue, CAPINAPExtensions extensions) {
    	if(timerID!=null)
    		this.timerID = new ASNTimerID(timerID);
    		   
        this.timerValue = new ASNInteger(timerValue,"TimerValue",0,Integer.MAX_VALUE,false);
        this.extensions = extensions;        
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.resetTimer_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.resetTimer;
    }

    @Override
    public TimerID getTimerID() {
    	if(timerID==null)
    		return TimerID.tssf;
    	
        return timerID.getType();
    }

    @Override
    public int getTimerValue() {
    	if(timerID==null || timerID.getValue()==null)
    		return 0;
    	
        return timerValue.getIntValue();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ResetTimerRequest [");
        this.addInvokeIdInfo(sb);

        if (this.timerID != null) {
            sb.append(", timerID=");
            sb.append(timerID.toString());
        }
        sb.append(", timerValue=");
        sb.append(timerValue);
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timerValue==null)
			throw new ASNParsingComponentException("timer value should be set for reset timer request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
