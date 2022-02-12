package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLIPAPermissionImpl extends ASNEnumerated {
	public ASNLIPAPermissionImpl() {
		super("LIPAPermission",0,2,false);
	}
	
	public ASNLIPAPermissionImpl(LIPAPermission t) {
		super(t.getCode(),"LIPAPermission",0,2,false);
	}
	
	public LIPAPermission getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LIPAPermission.getInstance(realValue);
	}
}