package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRegionalSubscriptionResponse extends ASNEnumerated {
	public ASNRegionalSubscriptionResponse() {
		super("RegionalSubscriptionResponse",0,3,false);
	}
	
	public ASNRegionalSubscriptionResponse(RegionalSubscriptionResponse t) {
		super(t.getCode(),"RegionalSubscriptionResponse",0,3,false);
	}
	
	public RegionalSubscriptionResponse getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RegionalSubscriptionResponse.getInstance(realValue);
	}
}
