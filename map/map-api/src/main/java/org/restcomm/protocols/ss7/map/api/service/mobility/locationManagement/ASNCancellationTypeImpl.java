package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCancellationTypeImpl extends ASNEnumerated {
	public void setType(CancellationType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CancellationType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CancellationType.getInstance(getValue().intValue());
	}
}
