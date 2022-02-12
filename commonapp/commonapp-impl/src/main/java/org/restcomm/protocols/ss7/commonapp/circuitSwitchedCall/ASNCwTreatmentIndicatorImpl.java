package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CwTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCwTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNCwTreatmentIndicatorImpl() {
		super("CwTreatmentIndicator",1,2,false);
	}
	
	public ASNCwTreatmentIndicatorImpl(CwTreatmentIndicator t) {
		super(t.getCode(),"CwTreatmentIndicator",1,2,false);
	}
	
	public CwTreatmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CwTreatmentIndicator.getInstance(realValue);
	}
}
