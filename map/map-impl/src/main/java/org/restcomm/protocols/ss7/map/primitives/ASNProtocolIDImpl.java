package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNProtocolIDImpl extends ASNEnumerated {
	public void setType(ProtocolId t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ProtocolId getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ProtocolId.getProtocolId(getValue().intValue());
	}
}
