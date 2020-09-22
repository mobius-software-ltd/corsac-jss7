package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUESRVCCCapabilityImpl extends ASNEnumerated {
	public void setType(UESRVCCCapability t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public UESRVCCCapability getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return UESRVCCCapability.getInstance(getValue().intValue());
	}
}
