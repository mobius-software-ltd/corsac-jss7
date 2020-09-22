package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNOBcsmTriggerDetectionPoint extends ASNEnumerated {
	public void setType(OBcsmTriggerDetectionPoint t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public OBcsmTriggerDetectionPoint getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return OBcsmTriggerDetectionPoint.getInstance(getValue().intValue());
	}
}
