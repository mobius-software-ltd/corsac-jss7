package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,lengthIndefinite=false)
public class ASNAdditionalRoamingNotAllowedCauseImpl extends ASNEnumerated {
	public ASNAdditionalRoamingNotAllowedCauseImpl() {
		
	}
	
	public ASNAdditionalRoamingNotAllowedCauseImpl(AdditionalRoamingNotAllowedCause t) {
		super(t.getCode());
	}
	
	public AdditionalRoamingNotAllowedCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AdditionalRoamingNotAllowedCause.getInstance(realValue);
	}
}