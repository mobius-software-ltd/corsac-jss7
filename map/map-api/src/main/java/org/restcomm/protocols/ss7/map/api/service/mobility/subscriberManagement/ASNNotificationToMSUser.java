package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNotificationToMSUser extends ASNEnumerated {
	public void setType(NotificationToMSUser t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public NotificationToMSUser getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return NotificationToMSUser.getInstance(getValue().intValue());
	}
}
