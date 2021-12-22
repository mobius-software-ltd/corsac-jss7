package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEventTypeBCSMImpl extends ASNEnumerated {
	public void setType(EventTypeBCSM t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public EventTypeBCSM getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return EventTypeBCSM.getInstance(getValue().intValue());
	}
}
