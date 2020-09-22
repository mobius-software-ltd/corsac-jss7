package org.restcomm.protocols.ss7.map.api.errors;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,lengthIndefinite=false)
public class ASNPositionMethodFailureDiagnosticImpl extends ASNEnumerated {
	public void setType(PositionMethodFailureDiagnostic t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public PositionMethodFailureDiagnostic getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return PositionMethodFailureDiagnostic.getInstance(getValue().intValue());
	}
}
