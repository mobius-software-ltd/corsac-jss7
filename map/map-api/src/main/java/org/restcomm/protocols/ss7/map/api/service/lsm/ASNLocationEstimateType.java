package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLocationEstimateType extends ASNEnumerated {
	public void setType(LocationEstimateType t) {
		super.setValue(Long.valueOf(t.getType()));
	}
	
	public LocationEstimateType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LocationEstimateType.getLocationEstimateType(getValue().intValue());
	}
}
