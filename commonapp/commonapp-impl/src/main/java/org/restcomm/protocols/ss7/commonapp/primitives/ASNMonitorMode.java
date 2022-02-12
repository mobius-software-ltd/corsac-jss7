package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMonitorMode extends ASNEnumerated {
	public ASNMonitorMode() {
		super("MonitorMode",0,2,false);
	}
	
	public ASNMonitorMode(MonitorMode t) {
		super(t.getCode(),"MonitorMode",0,2,false);
	}
	
	public MonitorMode getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MonitorMode.getInstance(realValue);
	}
}