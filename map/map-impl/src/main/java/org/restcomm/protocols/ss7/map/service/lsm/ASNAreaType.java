package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAreaType extends ASNEnumerated {
	public ASNAreaType() {
		
	}
	
	public ASNAreaType(AreaType t) {
		super(t.getType());
	}
	
	public AreaType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AreaType.getAreaType(realValue);
	}
}
