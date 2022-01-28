package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CwTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCwTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNCwTreatmentIndicatorImpl() {
		
	}
	
	public ASNCwTreatmentIndicatorImpl(CwTreatmentIndicator t) {
		super(t.getCode());
	}
	
	public CwTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CwTreatmentIndicator.getInstance(realValue);
	}
}
