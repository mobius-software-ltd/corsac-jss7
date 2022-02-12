package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTypeOfUpdateImpl extends ASNEnumerated {
	public ASNTypeOfUpdateImpl() {
		super("TypeOfUpdate",0,1,false);
	}
	
	public ASNTypeOfUpdateImpl(TypeOfUpdate t) {
		super(t.getCode(),"TypeOfUpdate",0,1,false);
	}
	
	public TypeOfUpdate getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return TypeOfUpdate.getInstance(realValue);
	}
}