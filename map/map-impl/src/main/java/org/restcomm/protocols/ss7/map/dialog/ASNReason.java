package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.Reason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNReason extends ASNEnumerated {
	public void setType(Reason t) {
		super.setValue(new Long(t.getCode()));
	}
	
	public Reason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return Reason.getReason(getValue().intValue());
	}
}