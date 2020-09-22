package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGMLCRestriction extends ASNEnumerated {
	public void setType(GMLCRestriction t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public GMLCRestriction getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return GMLCRestriction.getInstance(getValue().intValue());
	}
}
