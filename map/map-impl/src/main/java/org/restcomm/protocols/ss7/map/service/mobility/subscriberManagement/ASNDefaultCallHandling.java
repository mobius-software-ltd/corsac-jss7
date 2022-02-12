package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNDefaultCallHandling extends ASNEnumerated {
	public ASNDefaultCallHandling() {
		super("DefaultCallHandling",0,1,false);
	}
	
	public ASNDefaultCallHandling(DefaultCallHandling t) {
		super(t.getCode(),"DefaultCallHandling",0,1,false);
	}
	
	public DefaultCallHandling getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return DefaultCallHandling.getInstance(realValue);
	}
}
