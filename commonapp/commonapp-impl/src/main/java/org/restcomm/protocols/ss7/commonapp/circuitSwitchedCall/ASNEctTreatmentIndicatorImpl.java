package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EctTreatmentIndicator;

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
