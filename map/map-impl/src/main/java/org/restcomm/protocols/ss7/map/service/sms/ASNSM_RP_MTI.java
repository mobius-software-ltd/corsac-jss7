package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSM_RP_MTI extends ASNEnumerated {
	public void setType(SM_RP_MTI t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public SM_RP_MTI getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return SM_RP_MTI.getInstance(getValue().intValue());
	}
}
