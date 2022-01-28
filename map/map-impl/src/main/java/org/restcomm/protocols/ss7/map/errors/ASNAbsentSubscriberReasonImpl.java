package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAbsentSubscriberReasonImpl extends ASNEnumerated {
	public ASNAbsentSubscriberReasonImpl() {
		
	}
	
	public ASNAbsentSubscriberReasonImpl(AbsentSubscriberReason t) {
		super(t.getCode());
	}
	
	public AbsentSubscriberReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AbsentSubscriberReason.getInstance(realValue);
	}
}
