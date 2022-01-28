package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTerminateCause extends ASNEnumerated {
	public ASNTerminateCause() {
		
	}
	
	public ASNTerminateCause(TerminationCause t) {
		super(t.getCause());
	}
	
	public TerminationCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return TerminationCause.getTerminationCause(realValue);
	}
}