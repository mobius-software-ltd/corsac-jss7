package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNPDPInitiationTypeImpl extends ASNEnumerated {
	public void setType(PDPInitiationType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public PDPInitiationType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return PDPInitiationType.getInstance(getValue().intValue());
	}
}
