package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepth;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTraceDepthImpl extends ASNEnumerated {
	public ASNTraceDepthImpl() {
		
	}
	
	public ASNTraceDepthImpl(TraceDepth t) {
		super(t.getCode());
	}
	
	public TraceDepth getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return TraceDepth.getInstance(realValue);
	}
}
