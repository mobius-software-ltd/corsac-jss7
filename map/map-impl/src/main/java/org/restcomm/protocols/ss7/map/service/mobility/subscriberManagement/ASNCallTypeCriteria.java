package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallTypeCriteria extends ASNEnumerated {
	public ASNCallTypeCriteria() {
		super("CallTypeCriteria",0,1,false);
	}
	
	public ASNCallTypeCriteria(CallTypeCriteria t) {
		super(t.getCode(),"CallTypeCriteria",0,1,false);
	}
	
	public CallTypeCriteria getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CallTypeCriteria.getInstance(realValue);
	}
}
