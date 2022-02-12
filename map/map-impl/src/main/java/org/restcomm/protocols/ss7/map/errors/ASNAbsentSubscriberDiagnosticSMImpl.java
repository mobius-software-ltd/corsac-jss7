package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAbsentSubscriberDiagnosticSMImpl extends ASNEnumerated {
	public ASNAbsentSubscriberDiagnosticSMImpl() {
		super("AbsentSubscriberDiagnosticSM",0,12,false);
	}
	
	public ASNAbsentSubscriberDiagnosticSMImpl(AbsentSubscriberDiagnosticSM t) {
		super(t.getCode(),"AbsentSubscriberDiagnosticSM",0,12,false);
	}
	
	public AbsentSubscriberDiagnosticSM getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AbsentSubscriberDiagnosticSM.getInstance(realValue);
	}
}
