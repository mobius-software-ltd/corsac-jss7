package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGPRSTriggerDetectionPoint extends ASNEnumerated {
	public ASNGPRSTriggerDetectionPoint() {
		
	}
	
	public ASNGPRSTriggerDetectionPoint(GPRSTriggerDetectionPoint t) {
		super(t.getCode());
	}
	
	public GPRSTriggerDetectionPoint getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return GPRSTriggerDetectionPoint.getInstance(realValue);
	}
}