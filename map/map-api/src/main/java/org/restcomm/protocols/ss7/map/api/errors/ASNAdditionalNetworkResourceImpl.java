package org.restcomm.protocols.ss7.map.api.errors;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAdditionalNetworkResourceImpl extends ASNEnumerated {
	public void setType(AdditionalNetworkResource t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AdditionalNetworkResource getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AdditionalNetworkResource.getInstance(getValue().intValue());
	}
}
