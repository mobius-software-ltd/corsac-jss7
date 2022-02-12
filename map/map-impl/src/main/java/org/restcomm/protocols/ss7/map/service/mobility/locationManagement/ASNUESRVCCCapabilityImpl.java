package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNUESRVCCCapabilityImpl extends ASNEnumerated {
	public ASNUESRVCCCapabilityImpl() {
		super("UESRVCCCapability",0,1,false);
	}
	
	public ASNUESRVCCCapabilityImpl(UESRVCCCapability t) {
		super(t.getCode(),"UESRVCCCapability",0,1,false);
	}
	
	public UESRVCCCapability getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return UESRVCCCapability.getInstance(realValue);
	}
}