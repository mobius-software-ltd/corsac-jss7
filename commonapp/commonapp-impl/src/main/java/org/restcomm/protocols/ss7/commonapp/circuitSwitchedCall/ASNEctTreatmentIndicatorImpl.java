package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EctTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEctTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNEctTreatmentIndicatorImpl() {
		super("EctTreatmentIndicator",1,2,false);
	}
	
	public ASNEctTreatmentIndicatorImpl(EctTreatmentIndicator t) {
		super(t.getCode(),"EctTreatmentIndicator",1,2,false);
	}
	
	public EctTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return EctTreatmentIndicator.getInstance(realValue);
	}
}