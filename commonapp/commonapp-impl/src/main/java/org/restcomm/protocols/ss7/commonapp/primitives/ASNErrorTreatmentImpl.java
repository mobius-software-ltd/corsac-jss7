package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNErrorTreatmentImpl extends ASNEnumerated {
	public void setType(ErrorTreatment t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ErrorTreatment getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ErrorTreatment.getInstance(getValue().intValue());
	}
}