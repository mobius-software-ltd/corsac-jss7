package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNOBcsmTriggerDetectionPoint extends ASNEnumerated {
	public ASNOBcsmTriggerDetectionPoint() {
		super("OBcsmTriggerDetectionPoint",2,4,false);
	}
	
	public ASNOBcsmTriggerDetectionPoint(OBcsmTriggerDetectionPoint t) {
		super(t.getCode(),"OBcsmTriggerDetectionPoint",2,4,false);
	}
	
	public OBcsmTriggerDetectionPoint getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return OBcsmTriggerDetectionPoint.getInstance(realValue);
	}
}
