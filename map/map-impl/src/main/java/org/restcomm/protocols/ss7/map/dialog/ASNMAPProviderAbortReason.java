package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMAPProviderAbortReason extends ASNEnumerated {
	public ASNMAPProviderAbortReason() {
		
	}
	
	public ASNMAPProviderAbortReason(MAPProviderAbortReason t) {
		super(t.getCode());
	}
	
	public MAPProviderAbortReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MAPProviderAbortReason.getInstance(realValue);
	}
}