package org.restcomm.protocols.ss7.map.api.service.sms;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMDeliveryNotIntended extends ASNEnumerated {
	public void setType(SMDeliveryNotIntended t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public SMDeliveryNotIntended getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return SMDeliveryNotIntended.getInstance(getValue().intValue());
	}
}
