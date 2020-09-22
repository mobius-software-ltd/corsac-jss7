package org.restcomm.protocols.ss7.map.api.dialog;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,lengthIndefinite=false)
public class ASNResourceUnavailableReason extends ASNEnumerated {
	public void setType(ResourceUnavailableReason t) {
		super.setValue(new Long(t.getCode()));
	}
	
	public ResourceUnavailableReason getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ResourceUnavailableReason.getInstance(getValue().intValue());
	}
}