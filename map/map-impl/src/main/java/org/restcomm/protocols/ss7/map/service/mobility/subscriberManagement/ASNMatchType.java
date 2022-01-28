package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMatchType extends ASNEnumerated {
	public ASNMatchType() {
		
	}
	
	public ASNMatchType(MatchType t) {
		super(t.getCode());
	}
	
	public MatchType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MatchType.getInstance(realValue);
	}
}