package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNetworkResourceImpl extends ASNEnumerated {
	public ASNNetworkResourceImpl() {
		super("NetworkResource",0,7,false);
	}
	
	public ASNNetworkResourceImpl(NetworkResource t) {
		super(t.getCode(),"NetworkResource",0,7,false);
	}
	
	public NetworkResource getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NetworkResource.getInstance(realValue);
	}
}
