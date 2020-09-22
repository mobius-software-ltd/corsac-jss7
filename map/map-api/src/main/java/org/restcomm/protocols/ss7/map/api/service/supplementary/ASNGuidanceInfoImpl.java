package org.restcomm.protocols.ss7.map.api.service.supplementary;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGuidanceInfoImpl extends ASNEnumerated {
	public void setType(GuidanceInfo t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public GuidanceInfo getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return GuidanceInfo.getInstance(getValue().intValue());
	}
}
