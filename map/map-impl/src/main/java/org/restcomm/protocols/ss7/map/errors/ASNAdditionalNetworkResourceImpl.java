package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.AdditionalNetworkResource;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAdditionalNetworkResourceImpl extends ASNEnumerated {
	public ASNAdditionalNetworkResourceImpl() {
		
	}
	
	public ASNAdditionalNetworkResourceImpl(AdditionalNetworkResource t) {
		super(t.getCode());
	}
	
	public AdditionalNetworkResource getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AdditionalNetworkResource.getInstance(realValue);
	}
}
