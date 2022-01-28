package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEventTypeBCSM extends ASNEnumerated {
	public ASNEventTypeBCSM() {
		
	}
	
	public ASNEventTypeBCSM(EventTypeBCSM t) {
		super(t.getCode());
	}
	
	public EventTypeBCSM getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return EventTypeBCSM.getInstance(realValue);
	}
}