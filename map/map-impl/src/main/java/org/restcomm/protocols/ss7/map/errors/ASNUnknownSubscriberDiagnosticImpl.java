package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.UnknownSubscriberDiagnostic;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUnknownSubscriberDiagnosticImpl extends ASNEnumerated {
	public void setType(UnknownSubscriberDiagnostic t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public UnknownSubscriberDiagnostic getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return UnknownSubscriberDiagnostic.getInstance(getValue().intValue());
	}
}
