package org.restcomm.protocols.ss7.tcap.asn;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

public class ASNResultType extends ASNInteger {
	public void setType(ResultType t) {
		super.setValue(t.getType());
	}
	
	public ResultType getType() throws ParseException {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ResultType.getFromInt(getValue());
	}
}
