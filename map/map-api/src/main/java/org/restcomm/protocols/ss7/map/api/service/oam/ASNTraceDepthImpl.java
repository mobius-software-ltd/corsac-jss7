package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTraceDepthImpl extends ASNEnumerated {
	public void setType(TraceDepth t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public TraceDepth getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return TraceDepth.getInstance(getValue().intValue());
	}
}
