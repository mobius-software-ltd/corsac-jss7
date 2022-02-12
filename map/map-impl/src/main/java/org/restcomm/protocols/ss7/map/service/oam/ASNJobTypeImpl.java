package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.JobType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNJobTypeImpl extends ASNEnumerated {
	public ASNJobTypeImpl() {
		super("JobType",0,3,false);
	}
	
	public ASNJobTypeImpl(JobType t) {
		super(t.getCode(),"JobType",0,3,false);
	}
	
	public JobType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return JobType.getInstance(realValue);
	}
}