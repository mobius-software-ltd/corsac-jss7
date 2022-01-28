package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCGEncountered extends ASNEnumerated {
	public ASNCGEncountered() {
		
	}
	
	public ASNCGEncountered(CGEncountered t) {
		super(t.getCode());
	}
	
	public CGEncountered getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CGEncountered.getInstance(realValue);
	}
}