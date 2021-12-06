package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRequestedCamelSubscriptionInfoImpl extends ASNEnumerated {
	public void setType(RequestedCAMELSubscriptionInfo t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public RequestedCAMELSubscriptionInfo getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return RequestedCAMELSubscriptionInfo.getInstance(getValue().intValue());
	}
}
