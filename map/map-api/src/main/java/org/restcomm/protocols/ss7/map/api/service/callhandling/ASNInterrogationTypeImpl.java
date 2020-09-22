package org.restcomm.protocols.ss7.map.api.service.callhandling;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNInterrogationTypeImpl extends ASNEnumerated {
	public void setType(InterrogationType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public InterrogationType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return InterrogationType.getInterrogationType(getValue().intValue());
	}
}
