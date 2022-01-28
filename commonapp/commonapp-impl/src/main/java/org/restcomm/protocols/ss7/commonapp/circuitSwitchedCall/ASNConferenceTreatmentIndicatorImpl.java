package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConferenceTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNConferenceTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNConferenceTreatmentIndicatorImpl() {
		
	}
	public ASNConferenceTreatmentIndicatorImpl(ConferenceTreatmentIndicator t) {
		super(t.getCode());
	}
	
	public ConferenceTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ConferenceTreatmentIndicator.getInstance(realValue);
	}
}