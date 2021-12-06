package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMAPProviderAbortReason extends ASNEnumerated {
	public void setType(MAPProviderAbortReason t) {
		super.setValue(new Long(t.getCode()));
	}
	
	public MAPProviderAbortReason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return MAPProviderAbortReason.getInstance(getValue().intValue());
	}
}