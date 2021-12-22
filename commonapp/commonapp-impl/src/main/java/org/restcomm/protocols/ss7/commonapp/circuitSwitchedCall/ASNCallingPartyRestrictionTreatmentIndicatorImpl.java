package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallingPartyRestrictionIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallingPartyRestrictionTreatmentIndicatorImpl extends ASNEnumerated {
	public void setType(CallingPartyRestrictionIndicator t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CallingPartyRestrictionIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CallingPartyRestrictionIndicator.getInstance(getValue().intValue());
	}
}
