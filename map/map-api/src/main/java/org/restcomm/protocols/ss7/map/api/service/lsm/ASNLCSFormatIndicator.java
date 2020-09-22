package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSFormatIndicator extends ASNEnumerated {
	public void setType(LCSFormatIndicator t) {
		super.setValue(Long.valueOf(t.getIndicator()));
	}
	
	public LCSFormatIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LCSFormatIndicator.getLCSFormatIndicator(getValue().intValue());
	}
}
