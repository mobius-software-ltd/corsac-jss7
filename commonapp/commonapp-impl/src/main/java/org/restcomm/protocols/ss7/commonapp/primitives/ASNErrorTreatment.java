package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNErrorTreatment extends ASNEnumerated {
	public ASNErrorTreatment() {
		super("ErrorTreatment",0,2,false);
	}
	
	public ASNErrorTreatment(ErrorTreatment t) {
		super(t.getCode(),"ErrorTreatment",0,2,false);
	}
	
	public ErrorTreatment getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ErrorTreatment.getInstance(realValue);
	}
}