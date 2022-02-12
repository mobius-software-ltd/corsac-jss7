package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRANTechnology extends ASNEnumerated {
	public ASNRANTechnology() {
		super("RANTechnology",0,1,false);
	}
	
	public ASNRANTechnology(RANTechnology t) {
		super(t.getCode(),"RANTechnology",0,1,false);
	}
	
	public RANTechnology getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RANTechnology.getInstance(realValue);
	}
}
