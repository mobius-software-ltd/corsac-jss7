package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWAllocationType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNPDNGWAllocationType extends ASNEnumerated {
	public ASNPDNGWAllocationType() {
		
	}
	
	public ASNPDNGWAllocationType(PDNGWAllocationType t) {
		super(t.getCode());
	}
	
	public PDNGWAllocationType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return PDNGWAllocationType.getInstance(realValue);
	}
}
