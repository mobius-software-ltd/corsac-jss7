package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,lengthIndefinite=false)
public class ASNAdditionalRoamingNotAllowedCauseImpl extends ASNEnumerated {
	public void setType(AdditionalRoamingNotAllowedCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AdditionalRoamingNotAllowedCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AdditionalRoamingNotAllowedCause.getInstance(getValue().intValue());
	}
}
