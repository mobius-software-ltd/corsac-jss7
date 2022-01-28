package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCliRestrictionOptionImpl extends ASNEnumerated {
	public ASNCliRestrictionOptionImpl() {
		
	}
	
	public ASNCliRestrictionOptionImpl(CliRestrictionOption t) {
		super(t.getCode());
	}
	
	public CliRestrictionOption getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CliRestrictionOption.getInstance(realValue);
	}
}