package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNPrivacyCheckRelatedAction extends ASNEnumerated {
	public void setType(PrivacyCheckRelatedAction t) {
		super.setValue(Long.valueOf(t.getAction()));
	}
	
	public PrivacyCheckRelatedAction getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(getValue().intValue());
	}
}
