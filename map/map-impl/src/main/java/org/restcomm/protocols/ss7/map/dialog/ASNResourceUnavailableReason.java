package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,lengthIndefinite=false)
public class ASNResourceUnavailableReason extends ASNEnumerated {
	public ASNResourceUnavailableReason() {
		super("ResourceUnavailableReason",0,1,false);
	}
	
	public ASNResourceUnavailableReason(ResourceUnavailableReason t) {
		super(t.getCode(),"ResourceUnavailableReason",0,1,false);
	}
	
	public ResourceUnavailableReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ResourceUnavailableReason.getInstance(realValue);
	}
}