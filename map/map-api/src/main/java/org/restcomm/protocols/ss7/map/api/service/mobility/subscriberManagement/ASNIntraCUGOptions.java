package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNIntraCUGOptions extends ASNEnumerated {
	public void setType(IntraCUGOptions t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public IntraCUGOptions getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return IntraCUGOptions.getInstance(getValue().intValue());
	}
}
