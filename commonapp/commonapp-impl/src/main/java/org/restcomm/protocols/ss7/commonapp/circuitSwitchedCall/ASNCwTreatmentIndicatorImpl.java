package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CwTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCwTreatmentIndicatorImpl extends ASNEnumerated {
	public void setType(CwTreatmentIndicator t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CwTreatmentIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CwTreatmentIndicator.getInstance(getValue().intValue());
	}
}
