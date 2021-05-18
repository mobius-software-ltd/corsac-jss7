package org.restcomm.protocols.ss7.cap.api.service.sms.primitive;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMOSMSCauseImpl extends ASNEnumerated {
	public void setType(MOSMSCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public MOSMSCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return MOSMSCause.getInstance(getValue().intValue());
	}
}
