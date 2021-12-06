package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMTSMSTPDUType extends ASNEnumerated {
	public void setType(MTSMSTPDUType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public MTSMSTPDUType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return MTSMSTPDUType.getInstance(getValue().intValue());
	}
}
