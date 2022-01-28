package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDomainTypeImpl extends ASNEnumerated {
	public ASNDomainTypeImpl() {
		
	}
	
	public ASNDomainTypeImpl(DomainType t) {
		super(t.getType());
	}
	
	public DomainType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return DomainType.getInstance(realValue);
	}
}