package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,lengthIndefinite=false)
public class ASNPositionMethodFailureDiagnosticImpl extends ASNEnumerated {
	public ASNPositionMethodFailureDiagnosticImpl() {
		super("PositionMethodFailureDiagnostic",0,8,false);
	}
	
	public ASNPositionMethodFailureDiagnosticImpl(PositionMethodFailureDiagnostic t) {
		super(t.getCode(),"PositionMethodFailureDiagnostic",0,8,false);
	}
	
	public PositionMethodFailureDiagnostic getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return PositionMethodFailureDiagnostic.getInstance(realValue);
	}
}
