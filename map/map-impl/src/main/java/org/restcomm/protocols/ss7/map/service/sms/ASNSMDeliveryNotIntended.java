package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMDeliveryNotIntended extends ASNEnumerated {
	public ASNSMDeliveryNotIntended() {
		
	}
	
	public ASNSMDeliveryNotIntended(SMDeliveryNotIntended t) {
		super(t.getCode());
	}
	
	public SMDeliveryNotIntended getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SMDeliveryNotIntended.getInstance(realValue);
	}
}