package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.CUGRejectCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCUGRejectCauseImpl extends ASNEnumerated {
	public ASNCUGRejectCauseImpl() {
		super("CUGRejectCause",0,7,false);
	}
	
	public ASNCUGRejectCauseImpl(CUGRejectCause t) {
		super(t.getCode(),"CUGRejectCause",0,7,false);
	}
	
	public CUGRejectCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CUGRejectCause.getInstance(realValue);
	}
}
