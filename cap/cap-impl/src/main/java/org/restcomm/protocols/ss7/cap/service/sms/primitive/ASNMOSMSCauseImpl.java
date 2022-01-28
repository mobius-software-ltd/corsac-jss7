package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMOSMSCauseImpl extends ASNEnumerated {
	public ASNMOSMSCauseImpl() {
		
	}
	
	public ASNMOSMSCauseImpl(MOSMSCause t) {
		super(t.getCode());
	}
	
	public MOSMSCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MOSMSCause.getInstance(realValue);
	}
}