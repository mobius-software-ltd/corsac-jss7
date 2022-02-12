package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNOverrideCategoryImpl extends ASNEnumerated {
	public ASNOverrideCategoryImpl() {
		super("OverrideCategory",0,1,false);
	}
	
	public ASNOverrideCategoryImpl(OverrideCategory t) {
		super(t.getCode(),"OverrideCategory",0,1,false);
	}
	
	public OverrideCategory getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return OverrideCategory.getInstance(realValue);
	}
}
