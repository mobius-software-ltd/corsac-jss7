package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConnectedNumberTreatmentInd;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNConnectedNumberTreatmentIndicatorImpl extends ASNEnumerated {
	public void setType(ConnectedNumberTreatmentInd t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ConnectedNumberTreatmentInd getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ConnectedNumberTreatmentInd.getInstance(getValue().intValue());
	}
}
