package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNetworkAccessMode extends ASNEnumerated {
	public void setType(NetworkAccessMode t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public NetworkAccessMode getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return NetworkAccessMode.getInstance(getValue().intValue());
	}
}
