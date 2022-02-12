package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.LoggingInterval;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLoggingIntervalImpl extends ASNEnumerated {
	public ASNLoggingIntervalImpl() {
		super("LoggingInterval",0,7,false);
	}
	
	public ASNLoggingIntervalImpl(LoggingInterval t) {
		super(t.getCode(),"LoggingInterval",0,7,false);
	}
	
	public LoggingInterval getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LoggingInterval.getInstance(realValue);
	}
}