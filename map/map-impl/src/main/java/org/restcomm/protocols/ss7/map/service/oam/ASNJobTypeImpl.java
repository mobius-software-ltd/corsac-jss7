package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.JobType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNJobTypeImpl extends ASNEnumerated {
	public ASNJobTypeImpl() {
		
	}
	
	public ASNJobTypeImpl(JobType t) {
		super(t.getCode());
	}
	
	public JobType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return JobType.getInstance(realValue);
	}
}