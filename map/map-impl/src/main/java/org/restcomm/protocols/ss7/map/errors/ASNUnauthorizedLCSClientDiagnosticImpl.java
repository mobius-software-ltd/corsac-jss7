package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,lengthIndefinite=false)
public class ASNUnauthorizedLCSClientDiagnosticImpl extends ASNEnumerated {
	public ASNUnauthorizedLCSClientDiagnosticImpl() {
		super("UnauthorizedLCSClientDiagnostic",0,7,false);
	}
	
	public ASNUnauthorizedLCSClientDiagnosticImpl(UnauthorizedLCSClientDiagnostic t) {
		super(t.getCode(),"UnauthorizedLCSClientDiagnostic",0,7,false);
	}
	
	public UnauthorizedLCSClientDiagnostic getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return UnauthorizedLCSClientDiagnostic.getInstance(realValue);
	}
}