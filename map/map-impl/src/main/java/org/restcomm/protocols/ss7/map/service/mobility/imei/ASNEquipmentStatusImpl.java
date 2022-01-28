package org.restcomm.protocols.ss7.map.service.mobility.imei;

import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEquipmentStatusImpl extends ASNEnumerated {
	public ASNEquipmentStatusImpl() {
		
	}
	
	public ASNEquipmentStatusImpl(EquipmentStatus t) {
		super(t.getCode());
	}
	
	public EquipmentStatus getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return EquipmentStatus.getInstance(realValue);
	}
}