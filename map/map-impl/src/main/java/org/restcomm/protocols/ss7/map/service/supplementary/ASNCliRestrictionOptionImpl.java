package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCliRestrictionOptionImpl extends ASNEnumerated {
	public void setType(CliRestrictionOption t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public CliRestrictionOption getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return CliRestrictionOption.getInstance(getValue().intValue());
	}
}
