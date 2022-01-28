package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEventTypeSMSImpl extends ASNEnumerated {
	public ASNEventTypeSMSImpl() {
		
	}
	
	public ASNEventTypeSMSImpl(EventTypeSMS t) {
		super(t.getCode());
	}
	
	public EventTypeSMS getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return EventTypeSMS.getInstance(realValue);
	}
}
