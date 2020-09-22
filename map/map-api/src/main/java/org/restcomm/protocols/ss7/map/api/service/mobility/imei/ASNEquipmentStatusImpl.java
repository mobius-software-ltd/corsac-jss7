package org.restcomm.protocols.ss7.map.api.service.mobility.imei;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNEquipmentStatusImpl extends ASNEnumerated {
	public void setType(EquipmentStatus t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public EquipmentStatus getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return EquipmentStatus.getInstance(getValue().intValue());
	}
}
