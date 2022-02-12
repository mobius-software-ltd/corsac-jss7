package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGPRSEventTypeImpl extends ASNEnumerated {
	public ASNGPRSEventTypeImpl() {
		super("GPRSEventType",1,14,false);
	}
	
	public ASNGPRSEventTypeImpl(GPRSEventType t) {
		super(t.getCode(),"GPRSEventType",1,14,false);
	}
	
	public GPRSEventType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return GPRSEventType.getInstance(realValue);
	}
}