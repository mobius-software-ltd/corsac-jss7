package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSPriority extends ASNEnumerated {
	public ASNLCSPriority() {
		super("LCSPriority",0,1,false);
	}
	
	public ASNLCSPriority(LCSPriority t) {
		super(t.getCode(),"LCSPriority",0,1,false);
	}
	
	public LCSPriority getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LCSPriority.getInstance(realValue);
	}
}