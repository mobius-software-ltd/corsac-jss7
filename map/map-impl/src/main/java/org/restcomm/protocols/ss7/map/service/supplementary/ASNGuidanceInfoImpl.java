package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.GuidanceInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNGuidanceInfoImpl extends ASNEnumerated {
	public ASNGuidanceInfoImpl() {
		super("GuidanceInfo",0,2,false);
	}
	
	public ASNGuidanceInfoImpl(GuidanceInfo t) {
		super(t.getCode(),"GuidanceInfo",0,2,false);
	}
	
	public GuidanceInfo getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return GuidanceInfo.getInstance(realValue);
	}
}
