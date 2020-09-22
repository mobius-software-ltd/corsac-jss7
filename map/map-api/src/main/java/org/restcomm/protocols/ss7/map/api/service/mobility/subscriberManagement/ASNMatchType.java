package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMatchType extends ASNEnumerated {
	public void setType(MatchType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public MatchType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return MatchType.getInstance(getValue().intValue());
	}
}
