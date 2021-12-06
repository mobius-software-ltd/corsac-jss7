package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAbsentSubscriberReasonImpl extends ASNEnumerated {
	public void setType(AbsentSubscriberReason t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AbsentSubscriberReason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AbsentSubscriberReason.getInstance(getValue().intValue());
	}
}
