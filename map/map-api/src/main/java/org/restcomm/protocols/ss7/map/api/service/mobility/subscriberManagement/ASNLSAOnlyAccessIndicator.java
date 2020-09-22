package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLSAOnlyAccessIndicator extends ASNEnumerated {
	public void setType(LSAOnlyAccessIndicator t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public LSAOnlyAccessIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LSAOnlyAccessIndicator.getInstance(getValue().intValue());
	}
}
