package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDomainTypeImpl extends ASNEnumerated {
	public void setType(DomainType t) {
		super.setValue(Long.valueOf(t.getType()));
	}
	
	public DomainType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return DomainType.getInstance(getValue().intValue());
	}
}
