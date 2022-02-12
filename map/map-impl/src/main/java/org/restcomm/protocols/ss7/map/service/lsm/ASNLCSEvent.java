package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSEvent extends ASNEnumerated {
	public ASNLCSEvent() {
		super("LCSEvent",0,3,false);
	}
	
	public ASNLCSEvent(LCSEvent t) {
		super(t.getEvent(),"LCSEvent",0,3,false);
	}
	
	public LCSEvent getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LCSEvent.getLCSEvent(realValue);
	}
}