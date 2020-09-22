package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRegionalSubscriptionResponse extends ASNEnumerated {
	public void setType(RegionalSubscriptionResponse t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public RegionalSubscriptionResponse getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return RegionalSubscriptionResponse.getInstance(getValue().intValue());
	}
}
