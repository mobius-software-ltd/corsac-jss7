package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMDeliveryNotIntended extends ASNEnumerated {
	public ASNSMDeliveryNotIntended() {
		super("SMDeliveryNotIntended",0,1,false);
	}
	
	public ASNSMDeliveryNotIntended(SMDeliveryNotIntended t) {
		super(t.getCode(),"SMDeliveryNotIntended",0,1,false);
	}
	
	public SMDeliveryNotIntended getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SMDeliveryNotIntended.getInstance(realValue);
	}
}