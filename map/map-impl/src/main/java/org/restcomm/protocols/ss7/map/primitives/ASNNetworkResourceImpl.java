package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNetworkResourceImpl extends ASNEnumerated {
	public void setType(NetworkResource t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public NetworkResource getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return NetworkResource.getInstance(getValue().intValue());
	}
}
