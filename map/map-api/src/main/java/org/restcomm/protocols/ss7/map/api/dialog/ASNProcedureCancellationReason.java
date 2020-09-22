package org.restcomm.protocols.ss7.map.api.dialog;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,lengthIndefinite=false)
public class ASNProcedureCancellationReason extends ASNEnumerated {
	public void setType(ProcedureCancellationReason t) {
		super.setValue(new Long(t.getCode()));
	}
	
	public ProcedureCancellationReason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ProcedureCancellationReason.getInstance(getValue().intValue());
	}
}