package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRANTechnology extends ASNEnumerated {
	public void setType(RANTechnology t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public RANTechnology getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return RANTechnology.getInstance(getValue().intValue());
	}
}
