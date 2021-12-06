package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.CUGRejectCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCUGRejectCauseImpl extends ASNEnumerated {
	public void setType(CUGRejectCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CUGRejectCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CUGRejectCause.getInstance(getValue().intValue());
	}
}
