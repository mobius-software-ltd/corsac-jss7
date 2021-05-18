package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

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
