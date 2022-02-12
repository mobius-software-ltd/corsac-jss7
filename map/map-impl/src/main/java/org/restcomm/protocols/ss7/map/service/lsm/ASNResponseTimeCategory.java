package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNResponseTimeCategory extends ASNEnumerated {
	public ASNResponseTimeCategory() {
		super("ResponseTimeCategory",0,1,false);
	}
	
	public ASNResponseTimeCategory(ResponseTimeCategory t) {
		super(t.getCategory(),"ResponseTimeCategory",0,1,false);
	}
	
	public ResponseTimeCategory getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ResponseTimeCategory.getResponseTimeCategory(realValue);
	}
}