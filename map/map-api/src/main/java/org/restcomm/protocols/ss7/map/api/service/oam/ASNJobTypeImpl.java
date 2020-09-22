package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNJobTypeImpl extends ASNEnumerated {
	public void setType(JobType t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public JobType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return JobType.getInstance(getValue().intValue());
	}
}
