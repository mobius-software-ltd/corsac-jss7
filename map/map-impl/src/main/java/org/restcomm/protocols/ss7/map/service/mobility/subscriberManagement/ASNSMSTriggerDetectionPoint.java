package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMSTriggerDetectionPoint extends ASNEnumerated {
	public void setType(SMSTriggerDetectionPoint t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public SMSTriggerDetectionPoint getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return SMSTriggerDetectionPoint.getInstance(getValue().intValue());
	}
}
