package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNControlTypeImpl extends ASNEnumerated {
	public ASNControlTypeImpl() {
		super("ControlType",0,1,false);
	}
	
	public ASNControlTypeImpl(ControlType t) {
		super(t.getCode(),"ControlType",0,1,false);
	}
	
	public ControlType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ControlType.getInstance(realValue);
	}
}
