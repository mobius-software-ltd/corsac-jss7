package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTBcsmTriggerDetectionPoint extends ASNEnumerated {
	public void setType(TBcsmTriggerDetectionPoint t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public TBcsmTriggerDetectionPoint getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return TBcsmTriggerDetectionPoint.getInstance(getValue().intValue());
	}
}
