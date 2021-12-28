package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNumberingPlan extends ASNEnumerated {
	public void setType(NumberingPlan t) {
		super.setValue(Long.valueOf(t.getIndicator()));
	}
	
	public NumberingPlan getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return NumberingPlan.getInstance(getValue().intValue());
	}
}
