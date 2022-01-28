package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNForwardingReasonImpl extends ASNEnumerated {
	public ASNForwardingReasonImpl() {
		
	}
	
	public ASNForwardingReasonImpl(ForwardingReason t) {
		super(t.getCode());
	}
	
	public ForwardingReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ForwardingReason.getForwardingReason(realValue);
	}
}