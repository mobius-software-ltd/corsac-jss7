package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDefaultGPRSHandling extends ASNEnumerated {
	public ASNDefaultGPRSHandling() {
		super("DefaultGPRSHandling",0,1,false);
	}
	
	public ASNDefaultGPRSHandling(DefaultGPRSHandling t) {
		super(t.getCode(),"DefaultGPRSHandling",0,1,false);
	}
	
	public DefaultGPRSHandling getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return DefaultGPRSHandling.getInstance(realValue);
	}
}
