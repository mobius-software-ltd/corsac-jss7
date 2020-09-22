package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSClientInternalID extends ASNEnumerated {
	public void setType(LCSClientInternalID t) {
		super.setValue(Long.valueOf(t.getId()));
	}
	
	public LCSClientInternalID getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LCSClientInternalID.getLCSClientInternalID(getValue().intValue());
	}
}
