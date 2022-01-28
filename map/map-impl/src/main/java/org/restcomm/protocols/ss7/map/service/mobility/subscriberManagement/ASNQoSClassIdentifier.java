package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNQoSClassIdentifier extends ASNEnumerated {
	public ASNQoSClassIdentifier() {
		
	}
	
	public ASNQoSClassIdentifier(QoSClassIdentifier t) {
		super(t.getCode());
	}
	
	public QoSClassIdentifier getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return QoSClassIdentifier.getInstance(realValue);
	}
}
