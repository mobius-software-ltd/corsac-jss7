package org.restcomm.protocols.ss7.map.api.service.sms;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMDeliveryOutcome extends ASNEnumerated {
	public void setType(SMDeliveryOutcome t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public SMDeliveryOutcome getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return SMDeliveryOutcome.getInstance(getValue().intValue());
	}
}
