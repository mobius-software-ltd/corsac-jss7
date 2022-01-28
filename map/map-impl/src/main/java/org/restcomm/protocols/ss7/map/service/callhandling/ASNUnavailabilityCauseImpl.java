package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUnavailabilityCauseImpl extends ASNEnumerated {
	public ASNUnavailabilityCauseImpl() {
		
	}
	
	public ASNUnavailabilityCauseImpl(UnavailabilityCause t) {
		super(t.getCode());
	}
	
	public UnavailabilityCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return UnavailabilityCause.getUnavailabilityCause(realValue);
	}
}