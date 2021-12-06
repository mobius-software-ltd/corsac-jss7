package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTerminateCause extends ASNEnumerated {
	public void setType(TerminationCause t) {
		super.setValue(Long.valueOf(t.getCause()));
	}
	
	public TerminationCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return TerminationCause.getTerminationCause(getValue().intValue());
	}
}
