package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAdditionalRequestedCamelSubscriptionInfoImpl extends ASNEnumerated {
	public void setType(AdditionalRequestedCAMELSubscriptionInfo t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AdditionalRequestedCAMELSubscriptionInfo getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AdditionalRequestedCAMELSubscriptionInfo.getInstance(getValue().intValue());
	}
}
