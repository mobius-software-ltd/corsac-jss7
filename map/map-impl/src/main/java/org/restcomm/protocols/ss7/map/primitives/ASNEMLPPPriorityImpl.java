package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEMLPPPriorityImpl extends ASNEnumerated	 {
	public ASNEMLPPPriorityImpl() {
		
	}
	
	public ASNEMLPPPriorityImpl(EMLPPPriority t) {
		super(t.getCode());
	}
	
	public EMLPPPriority getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return EMLPPPriority.getEMLPPPriority(realValue);
	}
}