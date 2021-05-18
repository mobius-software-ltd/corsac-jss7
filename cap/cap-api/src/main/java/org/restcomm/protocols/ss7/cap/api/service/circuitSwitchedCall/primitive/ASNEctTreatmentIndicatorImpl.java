package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEctTreatmentIndicatorImpl extends ASNEnumerated {
	public void setType(EctTreatmentIndicator t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public EctTreatmentIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return EctTreatmentIndicator.getInstance(getValue().intValue());
	}
}
