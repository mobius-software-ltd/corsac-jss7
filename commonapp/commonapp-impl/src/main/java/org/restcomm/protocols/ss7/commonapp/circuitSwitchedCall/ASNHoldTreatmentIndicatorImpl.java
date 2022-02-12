package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.HoldTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNHoldTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNHoldTreatmentIndicatorImpl() {
		super("HoldTreatmentIndicator",1,2,false);
	}
	
	public ASNHoldTreatmentIndicatorImpl(HoldTreatmentIndicator t) {
		super(t.getCode(),"HoldTreatmentIndicator",1,2,false);
	}
	
	public HoldTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return HoldTreatmentIndicator.getInstance(realValue);
	}
}
