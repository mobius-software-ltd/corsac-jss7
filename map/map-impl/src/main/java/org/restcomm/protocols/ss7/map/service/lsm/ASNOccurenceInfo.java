package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNOccurenceInfo extends ASNEnumerated {
	public ASNOccurenceInfo() {
		
	}
	
	public ASNOccurenceInfo(OccurrenceInfo t) {
		super(t.getInfo());
	}
	
	public OccurrenceInfo getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return OccurrenceInfo.getOccurrenceInfo(realValue);
	}
}