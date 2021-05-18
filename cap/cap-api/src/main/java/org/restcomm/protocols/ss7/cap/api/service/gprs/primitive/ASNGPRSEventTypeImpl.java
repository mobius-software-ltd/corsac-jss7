package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGPRSEventTypeImpl extends ASNEnumerated {
	public void setType(GPRSEventType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public GPRSEventType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return GPRSEventType.getInstance(getValue().intValue());
	}
}
