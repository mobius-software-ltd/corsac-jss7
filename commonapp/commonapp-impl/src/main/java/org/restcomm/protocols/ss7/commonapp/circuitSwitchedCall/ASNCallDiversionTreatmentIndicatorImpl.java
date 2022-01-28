package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallDiversionTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallDiversionTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNCallDiversionTreatmentIndicatorImpl() {
		
	}
	
	public ASNCallDiversionTreatmentIndicatorImpl(CallDiversionTreatmentIndicator t) {
		super(t.getCode());
	}
	
	public CallDiversionTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CallDiversionTreatmentIndicator.getInstance(realValue);
	}
}
