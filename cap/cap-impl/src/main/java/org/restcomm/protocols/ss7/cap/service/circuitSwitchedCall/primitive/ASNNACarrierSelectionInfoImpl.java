package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierSelectionInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNACarrierSelectionInfoImpl extends ASNEnumerated {
	public ASNNACarrierSelectionInfoImpl() {
		super("NACarrierSelectionInfo",0,4,false);
	}
	
	public ASNNACarrierSelectionInfoImpl(NACarrierSelectionInfo t) {
		super(t.getCode(),"NACarrierSelectionInfo",0,4,false);
	}
	
	public NACarrierSelectionInfo getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NACarrierSelectionInfo.getInstance(realValue);
	}
}