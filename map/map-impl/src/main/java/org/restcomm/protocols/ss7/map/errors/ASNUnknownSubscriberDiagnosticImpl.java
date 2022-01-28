package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.UnknownSubscriberDiagnostic;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUnknownSubscriberDiagnosticImpl extends ASNEnumerated {
	public ASNUnknownSubscriberDiagnosticImpl() {
		
	}
	
	public ASNUnknownSubscriberDiagnosticImpl(UnknownSubscriberDiagnostic t) {
		super(Long.valueOf(t.getCode()));
	}
	
	public UnknownSubscriberDiagnostic getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return UnknownSubscriberDiagnostic.getInstance(realValue);
	}
}