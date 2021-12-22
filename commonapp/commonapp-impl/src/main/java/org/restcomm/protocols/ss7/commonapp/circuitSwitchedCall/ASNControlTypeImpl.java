package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNControlTypeImpl extends ASNEnumerated {
	public void setType(ControlType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ControlType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ControlType.getInstance(getValue().intValue());
	}
}
