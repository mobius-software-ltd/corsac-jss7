package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNFailureCause extends ASNEnumerated {
	public ASNFailureCause() {
		
	}
	
	public ASNFailureCause(FailureCause t) {
		super(t.getCode());
	}
	
	public FailureCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return FailureCause.getInstance(realValue);
	}
}