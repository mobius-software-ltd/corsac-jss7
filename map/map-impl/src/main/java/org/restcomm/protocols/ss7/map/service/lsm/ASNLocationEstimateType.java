package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLocationEstimateType extends ASNEnumerated {
	public ASNLocationEstimateType() {
		super("LocationEstimateType",0,4,false);
	}
	
	public ASNLocationEstimateType(LocationEstimateType t) {
		super(t.getType(),"LocationEstimateType",0,4,false);
	}
	
	public LocationEstimateType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LocationEstimateType.getLocationEstimateType(realValue);
	}
}
