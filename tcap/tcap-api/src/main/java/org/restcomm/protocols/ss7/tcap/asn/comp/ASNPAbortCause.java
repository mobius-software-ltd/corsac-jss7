package org.restcomm.protocols.ss7.tcap.asn.comp;

import org.restcomm.protocols.ss7.tcap.asn.ParseException;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x0A,constructed=false,lengthIndefinite=false)
public class ASNPAbortCause extends ASNEnumerated {
	public void setType(PAbortCauseType t) {
		super.setValue(Integer.valueOf(t.getType()).longValue());
	}
	
	public PAbortCauseType getType() throws ParseException {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return PAbortCauseType.getFromInt(getValue().intValue());
	}
}
