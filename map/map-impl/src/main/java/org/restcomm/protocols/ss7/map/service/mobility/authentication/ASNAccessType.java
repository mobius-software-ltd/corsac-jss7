package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AccessType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAccessType extends ASNEnumerated {
	public ASNAccessType() {
		super("AccessType",0,10,false);
	}
	
	public ASNAccessType(AccessType t) {
		super(t.getCode(),"AccessType",0,10,false);
	}
	
	public AccessType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AccessType.getInstance(realValue);
	}
}
