package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNQoSClassIdentifier extends ASNEnumerated {
	public void setType(QoSClassIdentifier t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public QoSClassIdentifier getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return QoSClassIdentifier.getInstance(getValue().intValue());
	}
}
