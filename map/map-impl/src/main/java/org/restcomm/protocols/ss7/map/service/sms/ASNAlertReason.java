package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAlertReason extends ASNEnumerated {
	public void setType(AlertReason t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AlertReason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AlertReason.getInstance(getValue().intValue());
	}
}
