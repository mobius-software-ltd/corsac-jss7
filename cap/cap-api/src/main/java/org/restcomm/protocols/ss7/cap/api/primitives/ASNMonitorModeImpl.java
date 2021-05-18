package org.restcomm.protocols.ss7.cap.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMonitorModeImpl extends ASNEnumerated {
	public void setType(MonitorMode t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public MonitorMode getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return MonitorMode.getInstance(getValue().intValue());
	}
}
