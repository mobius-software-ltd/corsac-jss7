package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNResponseTimeCategory extends ASNEnumerated {
	public void setType(ResponseTimeCategory t) {
		super.setValue(Long.valueOf(t.getCategory()));
	}
	
	public ResponseTimeCategory getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ResponseTimeCategory.getResponseTimeCategory(getValue().intValue());
	}
}
