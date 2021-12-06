package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAbsentSubscriberDiagnosticSMImpl extends ASNEnumerated {
	public void setType(AbsentSubscriberDiagnosticSM t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AbsentSubscriberDiagnosticSM getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AbsentSubscriberDiagnosticSM.getInstance(getValue().intValue());
	}
}
