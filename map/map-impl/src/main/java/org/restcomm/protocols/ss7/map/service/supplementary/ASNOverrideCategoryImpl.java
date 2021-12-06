package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNOverrideCategoryImpl extends ASNEnumerated {
	public void setType(OverrideCategory t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public OverrideCategory getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return OverrideCategory.getInstance(getValue().intValue());
	}
}
