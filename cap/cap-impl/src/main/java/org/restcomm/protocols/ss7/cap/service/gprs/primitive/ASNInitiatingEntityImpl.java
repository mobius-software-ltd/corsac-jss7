package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNInitiatingEntityImpl extends ASNEnumerated {
	public void setType(InitiatingEntity t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public InitiatingEntity getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return InitiatingEntity.getInstance(getValue().intValue());
	}
}
