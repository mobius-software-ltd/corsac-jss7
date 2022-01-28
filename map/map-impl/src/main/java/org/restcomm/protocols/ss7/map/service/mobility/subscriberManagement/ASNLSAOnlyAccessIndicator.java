package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAOnlyAccessIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLSAOnlyAccessIndicator extends ASNEnumerated {
	public ASNLSAOnlyAccessIndicator() {
		
	}
	
	public ASNLSAOnlyAccessIndicator(LSAOnlyAccessIndicator t) {
		super(t.getCode());
	}
	
	public LSAOnlyAccessIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LSAOnlyAccessIndicator.getInstance(realValue);
	}
}