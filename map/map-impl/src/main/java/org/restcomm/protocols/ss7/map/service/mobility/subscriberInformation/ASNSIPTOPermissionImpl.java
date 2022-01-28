package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSIPTOPermissionImpl extends ASNEnumerated {
	public ASNSIPTOPermissionImpl() {
		
	}
	
	public ASNSIPTOPermissionImpl(SIPTOPermission t) {
		super(t.getCode());
	}
	
	public SIPTOPermission getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SIPTOPermission.getInstance(realValue);
	}
}