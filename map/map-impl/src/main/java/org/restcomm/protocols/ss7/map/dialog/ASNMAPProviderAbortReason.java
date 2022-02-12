package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMAPProviderAbortReason extends ASNEnumerated {
	public ASNMAPProviderAbortReason() {
		super("MAPProviderAbortReason",0,1,false);
	}
	
	public ASNMAPProviderAbortReason(MAPProviderAbortReason t) {
		super(t.getCode(),"MAPProviderAbortReason",0,1,false);
	}
	
	public MAPProviderAbortReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MAPProviderAbortReason.getInstance(realValue);
	}
}