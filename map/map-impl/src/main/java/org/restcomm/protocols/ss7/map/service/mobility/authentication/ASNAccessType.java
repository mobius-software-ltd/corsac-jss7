package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AccessType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAccessType extends ASNEnumerated {
	public void setType(AccessType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AccessType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AccessType.getInstance(getValue().intValue());
	}
}
