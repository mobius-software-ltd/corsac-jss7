package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLoggingDurationImpl extends ASNEnumerated {
	public void setType(LoggingDuration t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public LoggingDuration getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LoggingDuration.getInstance(getValue().intValue());
	}
}
