package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNOccurenceInfo extends ASNEnumerated {
	public ASNOccurenceInfo() {
		super("OccurrenceInfo",0,1,false);
	}
	
	public ASNOccurenceInfo(OccurrenceInfo t) {
		super(t.getInfo(),"OccurrenceInfo",0,1,false);
	}
	
	public OccurrenceInfo getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return OccurrenceInfo.getOccurrenceInfo(realValue);
	}
}