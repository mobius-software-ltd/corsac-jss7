package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSubscriberStatus extends ASNEnumerated {
	public ASNSubscriberStatus() {
		
	}
	
	public ASNSubscriberStatus(SubscriberStatus t) {
		super(t.getCode());
	}
	
	public SubscriberStatus getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SubscriberStatus.getInstance(realValue);
	}
}