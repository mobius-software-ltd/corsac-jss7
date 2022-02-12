package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDefaultSMSHandling extends ASNEnumerated {
	public ASNDefaultSMSHandling() {
		super("DefaultSMSHandling",0,1,false);
	}
	
	public ASNDefaultSMSHandling(DefaultSMSHandling t) {
		super(t.getCode(),"DefaultSMSHandling",0,1,false);
	}
	
	public DefaultSMSHandling getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return DefaultSMSHandling.getInstance(realValue);
	}
}
