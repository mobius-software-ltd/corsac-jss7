package org.restcomm.protocols.ss7.tcap.asn.comp;

import org.restcomm.protocols.ss7.tcap.asn.ParseException;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=10,constructed=false,lengthIndefinite=false)
public class ASNPAbortCause extends ASNEnumerated {
	public ASNPAbortCause() {
		
	}
	
	public ASNPAbortCause(PAbortCauseType t) {
		super(t.getType());
	}
	
	public PAbortCauseType getType() throws ParseException {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return PAbortCauseType.getFromInt(realValue);
	}
}
