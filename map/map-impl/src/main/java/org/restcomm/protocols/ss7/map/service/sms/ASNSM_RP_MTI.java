package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSM_RP_MTI extends ASNEnumerated {
	public ASNSM_RP_MTI() {
		
	}
	
	public ASNSM_RP_MTI(SM_RP_MTI t) {
		super(t.getCode());
	}
	
	public SM_RP_MTI getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SM_RP_MTI.getInstance(realValue);
	}
}