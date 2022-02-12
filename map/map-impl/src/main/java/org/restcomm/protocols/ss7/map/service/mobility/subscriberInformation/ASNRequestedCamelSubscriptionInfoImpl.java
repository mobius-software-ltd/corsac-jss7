package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRequestedCamelSubscriptionInfoImpl extends ASNEnumerated {
	public ASNRequestedCamelSubscriptionInfoImpl() {
		super("RequestedCAMELSubscriptionInfo",0,8,false);
	}
	
	public ASNRequestedCamelSubscriptionInfoImpl(RequestedCAMELSubscriptionInfo t) {
		super(t.getCode(),"RequestedCAMELSubscriptionInfo",0,8,false);
	}
	
	public RequestedCAMELSubscriptionInfo getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RequestedCAMELSubscriptionInfo.getInstance(realValue);
	}
}
