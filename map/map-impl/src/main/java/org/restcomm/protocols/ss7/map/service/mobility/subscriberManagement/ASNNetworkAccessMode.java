package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNetworkAccessMode extends ASNEnumerated {
	public ASNNetworkAccessMode() {
		super("NetworkAccessMode",0,2,false);
	}
	
	public ASNNetworkAccessMode(NetworkAccessMode t) {
		super(t.getCode(),"NetworkAccessMode",0,2,false);
	}
	
	public NetworkAccessMode getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NetworkAccessMode.getInstance(realValue);
	}
}