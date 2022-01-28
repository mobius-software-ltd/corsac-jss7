package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,lengthIndefinite=false)
public class ASNProcedureCancellationReason extends ASNEnumerated {
	public ASNProcedureCancellationReason() {
		
	}
	
	public ASNProcedureCancellationReason(ProcedureCancellationReason t) {
		super(t.getCode());
	}
	
	public ProcedureCancellationReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ProcedureCancellationReason.getInstance(realValue);
	}
}