package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNRequestedInformationTypeImpl extends ASNEnumerated {
	public ASNRequestedInformationTypeImpl() {
		super("RequestedInformationType",0,30,false);
	}
	
	public ASNRequestedInformationTypeImpl(RequestedInformationType t) {
		super(t.getCode(),"RequestedInformationType",0,30,false);
	}
	
	public RequestedInformationType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RequestedInformationType.getInstance(realValue);
	}
}
