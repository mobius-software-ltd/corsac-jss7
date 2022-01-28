package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNInitiatingEntityImpl extends ASNEnumerated {
	public ASNInitiatingEntityImpl() {
		
	}
	
	public ASNInitiatingEntityImpl(InitiatingEntity t) {
		super(t.getCode());
	}
	
	public InitiatingEntity getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return InitiatingEntity.getInstance(realValue);
	}
}
