package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSClientType extends ASNEnumerated {
	public void setType(LCSClientType t) {
		super.setValue(Long.valueOf(t.getType()));
	}
	
	public LCSClientType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LCSClientType.getLCSClientType(getValue().intValue());
	}
}
