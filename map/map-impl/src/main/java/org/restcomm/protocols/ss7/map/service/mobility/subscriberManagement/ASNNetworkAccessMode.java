package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNetworkAccessMode extends ASNEnumerated {
	public ASNNetworkAccessMode() {
		
	}
	
	public ASNNetworkAccessMode(NetworkAccessMode t) {
		super(t.getCode());
	}
	
	public NetworkAccessMode getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NetworkAccessMode.getInstance(realValue);
	}
}