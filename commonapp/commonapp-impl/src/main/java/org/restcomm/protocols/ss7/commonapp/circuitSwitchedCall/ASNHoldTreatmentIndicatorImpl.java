package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.HoldTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNHoldTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNHoldTreatmentIndicatorImpl() {
		
	}
	
	public ASNHoldTreatmentIndicatorImpl(HoldTreatmentIndicator t) {
		super(t.getCode());
	}
	
	public HoldTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return HoldTreatmentIndicator.getInstance(realValue);
	}
}
