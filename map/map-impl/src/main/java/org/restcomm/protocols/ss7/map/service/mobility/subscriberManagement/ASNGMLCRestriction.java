package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGMLCRestriction extends ASNEnumerated {
	public ASNGMLCRestriction() {
		
	}
	
	public ASNGMLCRestriction(GMLCRestriction t) {
		super(t.getCode());
	}
	
	public GMLCRestriction getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return GMLCRestriction.getInstance(realValue);
	}
}