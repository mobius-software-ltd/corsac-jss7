package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConnectedNumberTreatmentInd;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNConnectedNumberTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNConnectedNumberTreatmentIndicatorImpl() {
		
	}
	
	public ASNConnectedNumberTreatmentIndicatorImpl(ConnectedNumberTreatmentInd t) {
		super(t.getCode());
	}
	
	public ConnectedNumberTreatmentInd getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ConnectedNumberTreatmentInd.getInstance(realValue);
	}
}