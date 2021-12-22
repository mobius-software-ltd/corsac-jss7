package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallDiversionTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallDiversionTreatmentIndicatorImpl extends ASNEnumerated {
	public void setType(CallDiversionTreatmentIndicator t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CallDiversionTreatmentIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CallDiversionTreatmentIndicator.getInstance(getValue().intValue());
	}
}
