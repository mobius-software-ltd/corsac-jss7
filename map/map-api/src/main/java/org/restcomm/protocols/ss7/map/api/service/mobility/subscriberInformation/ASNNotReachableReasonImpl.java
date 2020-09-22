package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNotReachableReasonImpl extends ASNEnumerated {
	public void setType(NotReachableReason t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public NotReachableReason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return NotReachableReason.getInstance(getValue().intValue());
	}
}
