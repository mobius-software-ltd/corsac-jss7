package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ControlType;

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
