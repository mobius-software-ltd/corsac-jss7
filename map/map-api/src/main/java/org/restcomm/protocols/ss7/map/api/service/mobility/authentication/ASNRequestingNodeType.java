package org.restcomm.protocols.ss7.map.api.service.mobility.authentication;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRequestingNodeType extends ASNEnumerated {
	public void setType(RequestingNodeType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public RequestingNodeType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return RequestingNodeType.getInstance(getValue().intValue());
	}
}
