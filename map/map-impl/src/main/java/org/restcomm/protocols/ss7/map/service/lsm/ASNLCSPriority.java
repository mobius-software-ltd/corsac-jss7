package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSPriority extends ASNEnumerated {
	public void setType(LCSPriority t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public LCSPriority getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LCSPriority.getInstance(getValue().intValue());
	}
}
