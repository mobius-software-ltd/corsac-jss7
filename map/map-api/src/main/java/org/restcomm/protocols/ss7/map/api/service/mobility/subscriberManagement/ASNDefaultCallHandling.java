package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDefaultCallHandling extends ASNEnumerated {
	public void setType(DefaultCallHandling t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public DefaultCallHandling getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return DefaultCallHandling.getInstance(getValue().intValue());
	}
}
