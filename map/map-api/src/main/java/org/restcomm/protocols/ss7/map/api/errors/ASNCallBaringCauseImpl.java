package org.restcomm.protocols.ss7.map.api.errors;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallBaringCauseImpl extends ASNEnumerated {
	public void setType(CallBarringCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CallBarringCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CallBarringCause.getInstance(getValue().intValue());
	}
}
