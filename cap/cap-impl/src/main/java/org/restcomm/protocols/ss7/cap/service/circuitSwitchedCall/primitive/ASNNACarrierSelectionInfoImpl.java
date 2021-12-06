package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierSelectionInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNACarrierSelectionInfoImpl extends ASNEnumerated {
	public void setType(NACarrierSelectionInfo t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public NACarrierSelectionInfo getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return NACarrierSelectionInfo.getInstance(getValue().intValue());
	}
}
