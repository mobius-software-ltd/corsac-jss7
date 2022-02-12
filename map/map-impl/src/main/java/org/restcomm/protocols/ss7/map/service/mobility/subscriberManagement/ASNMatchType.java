package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMatchType extends ASNEnumerated {
	public ASNMatchType() {
		super("MatchType",0,1,false);
	}
	
	public ASNMatchType(MatchType t) {
		super(t.getCode(),"MatchType",0,1,false);
	}
	
	public MatchType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MatchType.getInstance(realValue);
	}
}