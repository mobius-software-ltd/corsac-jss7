package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.HoldTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNHoldTreatmentIndicatorImpl extends ASNEnumerated {
	public void setType(HoldTreatmentIndicator t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public HoldTreatmentIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return HoldTreatmentIndicator.getInstance(getValue().intValue());
	}
}
