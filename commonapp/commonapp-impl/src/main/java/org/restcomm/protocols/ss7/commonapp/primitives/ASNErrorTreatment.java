package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNErrorTreatment extends ASNEnumerated {
	public ASNErrorTreatment() {
		
	}
	
	public ASNErrorTreatment(ErrorTreatment t) {
		super(t.getCode());
	}
	
	public ErrorTreatment getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ErrorTreatment.getInstance(realValue);
	}
}