package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTypeOfUpdateImpl extends ASNEnumerated {
	public ASNTypeOfUpdateImpl() {
		
	}
	
	public ASNTypeOfUpdateImpl(TypeOfUpdate t) {
		super(t.getCode());
	}
	
	public TypeOfUpdate getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return TypeOfUpdate.getInstance(realValue);
	}
}