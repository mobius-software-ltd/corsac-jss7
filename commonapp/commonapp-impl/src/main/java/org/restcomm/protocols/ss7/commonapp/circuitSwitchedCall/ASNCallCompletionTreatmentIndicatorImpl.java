package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallCompletionTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallCompletionTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNCallCompletionTreatmentIndicatorImpl() {
		super("CallCompletionTreatmentIndicator",1,2,false);
	}
	
	public ASNCallCompletionTreatmentIndicatorImpl(CallCompletionTreatmentIndicator t) {
		super(t.getCode(),"CallCompletionTreatmentIndicator",1,2,false);
	}
	
	public CallCompletionTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CallCompletionTreatmentIndicator.getInstance(realValue);
	}
}
