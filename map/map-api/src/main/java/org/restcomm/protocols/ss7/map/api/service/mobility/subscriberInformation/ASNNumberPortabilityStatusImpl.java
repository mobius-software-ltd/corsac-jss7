package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNumberPortabilityStatusImpl extends ASNEnumerated {
	public void setType(NumberPortabilityStatus t) {
		super.setValue(Long.valueOf(t.getType()));
	}
	
	public NumberPortabilityStatus getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return NumberPortabilityStatus.getInstance(getValue().intValue());
	}
}
