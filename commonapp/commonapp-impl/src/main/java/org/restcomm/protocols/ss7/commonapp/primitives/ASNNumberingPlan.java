package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNumberingPlan extends ASNEnumerated {
	public ASNNumberingPlan() {
		super("NumberingPlan",0,15,false);
	}
	
	public ASNNumberingPlan(NumberingPlan t) {
		super(t.getIndicator(),"NumberingPlan",0,15,false);
	}
	
	public NumberingPlan getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NumberingPlan.getInstance(realValue);
	}
}
