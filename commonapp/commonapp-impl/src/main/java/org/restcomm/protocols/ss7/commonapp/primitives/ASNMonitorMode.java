package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMonitorMode extends ASNEnumerated {
	public ASNMonitorMode() {
		
	}
	
	public ASNMonitorMode(MonitorMode t) {
		super(t.getCode());
	}
	
	public MonitorMode getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MonitorMode.getInstance(realValue);
	}
}