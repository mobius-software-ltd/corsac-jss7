package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCGEncounteredImpl extends ASNEnumerated {
	public void setType(CGEncountered t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CGEncountered getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CGEncountered.getInstance(getValue().intValue());
	}
}
