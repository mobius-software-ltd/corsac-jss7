package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUsedRatTypeImpl extends ASNEnumerated {
	public void setType(UsedRATType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public UsedRATType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return UsedRATType.getInstance(getValue().intValue());
	}
}
