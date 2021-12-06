package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLIPAPermissionImpl extends ASNEnumerated {
	public void setType(LIPAPermission t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public LIPAPermission getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return LIPAPermission.getInstance(getValue().intValue());
	}
}
