package org.restcomm.protocols.ss7.map.api.service.callhandling;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUnavailabilityCauseImpl extends ASNEnumerated {
	public void setType(UnavailabilityCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public UnavailabilityCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return UnavailabilityCause.getUnavailabilityCause(getValue().intValue());
	}
}
