package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAdditionalRequestedCamelSubscriptionInfoImpl extends ASNEnumerated {
	public ASNAdditionalRequestedCamelSubscriptionInfoImpl() {
		
	}
	
	public ASNAdditionalRequestedCamelSubscriptionInfoImpl(AdditionalRequestedCAMELSubscriptionInfo t) {
		super(t.getCode());
	}
	
	public AdditionalRequestedCAMELSubscriptionInfo getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AdditionalRequestedCAMELSubscriptionInfo.getInstance(realValue);
	}
}
