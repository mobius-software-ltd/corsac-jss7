package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRequestingNodeType extends ASNEnumerated {
	public ASNRequestingNodeType() {
		
	}
	
	public ASNRequestingNodeType(RequestingNodeType t) {
		super(t.getCode());
	}
	
	public RequestingNodeType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RequestingNodeType.getInstance(realValue);
	}
}
