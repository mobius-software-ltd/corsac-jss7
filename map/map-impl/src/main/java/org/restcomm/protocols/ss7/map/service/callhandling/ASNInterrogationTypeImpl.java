package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNInterrogationTypeImpl extends ASNEnumerated {
	public ASNInterrogationTypeImpl() {
		super("InterrogationType",0,1,false);
	}
	
	public ASNInterrogationTypeImpl(InterrogationType t) {
		super(t.getCode(),"InterrogationType",0,1,false);
	}
	
	public InterrogationType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return InterrogationType.getInterrogationType(realValue);
	}
}
