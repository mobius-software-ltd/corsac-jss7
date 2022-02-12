package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNumberPortabilityStatusImpl extends ASNEnumerated {
	public ASNNumberPortabilityStatusImpl() {
		super("NumberPortabilityStatus",0,5,false);
	}
	
	public ASNNumberPortabilityStatusImpl(NumberPortabilityStatus t) {
		super(t.getType(),"NumberPortabilityStatus",0,5,false);
	}
	
	public NumberPortabilityStatus getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NumberPortabilityStatus.getInstance(realValue);
	}
}