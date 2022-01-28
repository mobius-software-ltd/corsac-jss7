package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRequestedInformationTypeImpl extends ASNEnumerated {
	public ASNRequestedInformationTypeImpl() {
		
	}
	
	public ASNRequestedInformationTypeImpl(RequestedInformationType t) {
		super(t.getCode());
	}
	
	public RequestedInformationType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RequestedInformationType.getInstance(realValue);
	}
}
