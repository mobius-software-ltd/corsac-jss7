package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSEvent extends ASNEnumerated {
	public void setType(LCSEvent t) {
		super.setValue(Long.valueOf(t.getEvent()));
	}
	
	public LCSEvent getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LCSEvent.getLCSEvent(getValue().intValue());
	}
}
