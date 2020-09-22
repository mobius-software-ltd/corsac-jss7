package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDefaultGPRSHandling extends ASNEnumerated {
	public void setType(DefaultGPRSHandling t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public DefaultGPRSHandling getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return DefaultGPRSHandling.getInstance(getValue().intValue());
	}
}
