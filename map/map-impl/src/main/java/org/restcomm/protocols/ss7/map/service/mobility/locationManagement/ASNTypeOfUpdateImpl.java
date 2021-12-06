package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTypeOfUpdateImpl extends ASNEnumerated {
	public void setType(TypeOfUpdate t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public TypeOfUpdate getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return TypeOfUpdate.getInstance(getValue().intValue());
	}
}
