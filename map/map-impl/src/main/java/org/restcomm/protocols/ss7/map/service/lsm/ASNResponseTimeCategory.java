package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNResponseTimeCategory extends ASNEnumerated {
	public ASNResponseTimeCategory() {
		
	}
	
	public ASNResponseTimeCategory(ResponseTimeCategory t) {
		super(t.getCategory());
	}
	
	public ResponseTimeCategory getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ResponseTimeCategory.getResponseTimeCategory(realValue);
	}
}