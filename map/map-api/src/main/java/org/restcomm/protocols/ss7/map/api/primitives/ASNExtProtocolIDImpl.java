package org.restcomm.protocols.ss7.map.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNExtProtocolIDImpl extends ASNEnumerated {
	public void setType(ExtProtocolId t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ExtProtocolId getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ExtProtocolId.getExtProtocolId(getValue().intValue());
	}
}
