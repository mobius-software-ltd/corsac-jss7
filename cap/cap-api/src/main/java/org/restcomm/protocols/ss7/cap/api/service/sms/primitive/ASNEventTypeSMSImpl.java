package org.restcomm.protocols.ss7.cap.api.service.sms.primitive;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEventTypeSMSImpl extends ASNEnumerated {
	public void setType(EventTypeSMS t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public EventTypeSMS getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return EventTypeSMS.getInstance(getValue().intValue());
	}
}
