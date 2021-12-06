package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;

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
