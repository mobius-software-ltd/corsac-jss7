package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNPDNGWAllocationType extends ASNEnumerated {
	public void setType(PDNGWAllocationType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public PDNGWAllocationType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return PDNGWAllocationType.getInstance(getValue().intValue());
	}
}
