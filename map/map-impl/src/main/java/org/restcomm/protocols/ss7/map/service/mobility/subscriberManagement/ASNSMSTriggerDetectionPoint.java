package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMSTriggerDetectionPoint extends ASNEnumerated {
	public ASNSMSTriggerDetectionPoint() {
		
	}
	
	public ASNSMSTriggerDetectionPoint(SMSTriggerDetectionPoint t) {
		super(t.getCode());
	}
	
	public SMSTriggerDetectionPoint getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SMSTriggerDetectionPoint.getInstance(realValue);
	}
}