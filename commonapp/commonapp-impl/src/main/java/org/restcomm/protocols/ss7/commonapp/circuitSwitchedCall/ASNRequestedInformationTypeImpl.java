package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRequestedInformationTypeImpl extends ASNEnumerated {
	public void setType(RequestedInformationType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public RequestedInformationType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return RequestedInformationType.getInstance(getValue().intValue());
	}
}
