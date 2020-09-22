package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGPRSTriggerDetectionPoint extends ASNEnumerated {
	public void setType(GPRSTriggerDetectionPoint t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public GPRSTriggerDetectionPoint getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return GPRSTriggerDetectionPoint.getInstance(getValue().intValue());
	}
}
