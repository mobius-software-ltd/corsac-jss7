package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSIPTOPermissionImpl extends ASNEnumerated {
	public void setType(SIPTOPermission t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public SIPTOPermission getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return SIPTOPermission.getInstance(getValue().intValue());
	}
}
