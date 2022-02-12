package org.restcomm.protocols.ss7.tcap.asn;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

public class ASNResultType extends ASNInteger {
	public ASNResultType() {
		super(null,"ResultType",0,1,false);
	}
	
	public ASNResultType(ResultType t) {
		super(t.getType(),"ResultType",0,1,false);
	}
	
	public ResultType getType() throws ParseException {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ResultType.getFromInt(realValue);
	}
}
