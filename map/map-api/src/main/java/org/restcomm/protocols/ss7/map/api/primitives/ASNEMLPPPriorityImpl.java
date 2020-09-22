package org.restcomm.protocols.ss7.map.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEMLPPPriorityImpl extends ASNEnumerated {
	public void setType(EMLPPPriority t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public EMLPPPriority getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return EMLPPPriority.getEMLPPPriority(getValue().intValue());
	}
}
