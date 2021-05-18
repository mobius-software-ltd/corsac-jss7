package org.restcomm.protocols.ss7.cap.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCriticalityTypeImpl extends ASNEnumerated {
	public void setType(CriticalityType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CriticalityType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CriticalityType.getInstance(getValue().intValue());
	}
}
