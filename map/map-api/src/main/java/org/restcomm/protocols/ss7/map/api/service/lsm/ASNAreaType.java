package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAreaType extends ASNEnumerated {
	public void setType(AreaType t) {
		super.setValue(Long.valueOf(t.getType()));
	}
	
	public AreaType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AreaType.getAreaType(getValue().intValue());
	}
}
