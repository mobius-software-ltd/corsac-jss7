package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNOccurenceInfo extends ASNEnumerated {
	public void setType(OccurrenceInfo t) {
		super.setValue(Long.valueOf(t.getInfo()));
	}
	
	public OccurrenceInfo getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return OccurrenceInfo.getOccurrenceInfo(getValue().intValue());
	}
}
