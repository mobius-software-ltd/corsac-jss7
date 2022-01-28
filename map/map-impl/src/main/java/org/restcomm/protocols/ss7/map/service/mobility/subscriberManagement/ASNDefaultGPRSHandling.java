package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDefaultGPRSHandling extends ASNEnumerated {
	public ASNDefaultGPRSHandling() {
		
	}
	
	public ASNDefaultGPRSHandling(DefaultGPRSHandling t) {
		super(t.getCode());
	}
	
	public DefaultGPRSHandling getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return DefaultGPRSHandling.getInstance(realValue);
	}
}
