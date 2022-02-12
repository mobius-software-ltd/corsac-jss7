package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSubscriberStatus extends ASNEnumerated {
	public ASNSubscriberStatus() {
		super("SubscriberStatus",0,1,false);
	}
	
	public ASNSubscriberStatus(SubscriberStatus t) {
		super(t.getCode(),"SubscriberStatus",0,1,false);
	}
	
	public SubscriberStatus getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SubscriberStatus.getInstance(realValue);
	}
}