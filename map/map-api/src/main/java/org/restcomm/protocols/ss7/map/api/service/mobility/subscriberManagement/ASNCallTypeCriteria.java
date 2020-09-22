package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallTypeCriteria extends ASNEnumerated {
	public void setType(CallTypeCriteria t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CallTypeCriteria getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CallTypeCriteria.getInstance(getValue().intValue());
	}
}
