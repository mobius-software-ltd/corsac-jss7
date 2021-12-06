package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;

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
