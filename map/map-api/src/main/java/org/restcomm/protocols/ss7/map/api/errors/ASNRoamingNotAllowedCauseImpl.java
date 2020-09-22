package org.restcomm.protocols.ss7.map.api.errors;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRoamingNotAllowedCauseImpl extends ASNEnumerated {
	public void setType(RoamingNotAllowedCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public RoamingNotAllowedCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return RoamingNotAllowedCause.getInstance(getValue().intValue());
	}
}
