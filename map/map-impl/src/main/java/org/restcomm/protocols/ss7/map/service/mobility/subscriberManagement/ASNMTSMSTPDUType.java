package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMTSMSTPDUType extends ASNEnumerated {
	public ASNMTSMSTPDUType() {
		
	}
	
	public ASNMTSMSTPDUType(MTSMSTPDUType t) {
		super(t.getCode());
	}
	
	public MTSMSTPDUType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MTSMSTPDUType.getInstance(realValue);
	}
}