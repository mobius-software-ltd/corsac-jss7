package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;

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
