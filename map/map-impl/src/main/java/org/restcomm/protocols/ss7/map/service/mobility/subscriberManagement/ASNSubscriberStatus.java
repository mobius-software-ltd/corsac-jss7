package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSubscriberStatus extends ASNEnumerated {
	public void setType(SubscriberStatus t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public SubscriberStatus getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return SubscriberStatus.getInstance(getValue().intValue());
	}
}
