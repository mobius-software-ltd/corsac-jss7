package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUsedRatTypeImpl extends ASNEnumerated {
	public ASNUsedRatTypeImpl() {
		
	}
	
	public ASNUsedRatTypeImpl(UsedRATType t) {
		super(t.getCode());
	}
	
	public UsedRATType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return UsedRATType.getInstance(realValue);
	}
}