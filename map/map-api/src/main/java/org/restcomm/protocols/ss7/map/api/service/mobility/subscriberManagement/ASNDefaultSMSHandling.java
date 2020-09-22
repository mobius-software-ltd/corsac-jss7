package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDefaultSMSHandling extends ASNEnumerated {
	public void setType(DefaultSMSHandling t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public DefaultSMSHandling getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return DefaultSMSHandling.getInstance(getValue().intValue());
	}
}
