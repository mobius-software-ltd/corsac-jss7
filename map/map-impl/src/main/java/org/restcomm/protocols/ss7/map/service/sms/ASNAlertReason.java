package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAlertReason extends ASNEnumerated {
	public ASNAlertReason() {
		super("AlertReason",0,1,false);
	}
	
	public ASNAlertReason(AlertReason t) {
		super(t.getCode(),"AlertReason",0,1,false);
	}
	
	public AlertReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AlertReason.getInstance(realValue);
	}
}
