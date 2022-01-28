package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.LoggingDuration;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLoggingDurationImpl extends ASNEnumerated {
	public ASNLoggingDurationImpl() {
		
	}
	
	public ASNLoggingDurationImpl(LoggingDuration t) {
		super(t.getCode());
	}
	
	public LoggingDuration getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LoggingDuration.getInstance(realValue);
	}
}