package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLoggingIntervalImpl extends ASNEnumerated {
	public void setType(LoggingInterval t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public LoggingInterval getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LoggingInterval.getInstance(getValue().intValue());
	}
}
