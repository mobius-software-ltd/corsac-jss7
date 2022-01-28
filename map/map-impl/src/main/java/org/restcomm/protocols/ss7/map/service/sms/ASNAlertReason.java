package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAlertReason extends ASNEnumerated {
	public ASNAlertReason() {
		
	}
	
	public ASNAlertReason(AlertReason t) {
		super(t.getCode());
	}
	
	public AlertReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AlertReason.getInstance(realValue);
	}
}
