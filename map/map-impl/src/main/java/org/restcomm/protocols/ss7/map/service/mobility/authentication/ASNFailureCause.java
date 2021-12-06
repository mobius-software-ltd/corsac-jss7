package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNFailureCause extends ASNEnumerated {
	public void setType(FailureCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public FailureCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return FailureCause.getInstance(getValue().intValue());
	}
}
