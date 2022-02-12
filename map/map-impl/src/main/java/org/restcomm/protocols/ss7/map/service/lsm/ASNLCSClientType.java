package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSClientType extends ASNEnumerated {
	public ASNLCSClientType() {
		super("LCSClientType",0,3,false);
	}
	
	public ASNLCSClientType(LCSClientType t) {
		super(t.getType(),"LCSClientType",0,3,false);
	}
	
	public LCSClientType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LCSClientType.getLCSClientType(realValue);
	}
}