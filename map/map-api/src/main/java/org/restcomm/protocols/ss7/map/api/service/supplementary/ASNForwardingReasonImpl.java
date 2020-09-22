package org.restcomm.protocols.ss7.map.api.service.supplementary;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNForwardingReasonImpl extends ASNEnumerated {
	public void setType(ForwardingReason t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ForwardingReason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ForwardingReason.getForwardingReason(getValue().intValue());
	}
}
