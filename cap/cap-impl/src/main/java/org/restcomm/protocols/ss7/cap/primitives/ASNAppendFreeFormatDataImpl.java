package org.restcomm.protocols.ss7.cap.primitives;

import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAppendFreeFormatDataImpl extends ASNEnumerated {
	public void setType(AppendFreeFormatData t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public AppendFreeFormatData getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AppendFreeFormatData.getInstance(getValue().intValue());
	}
}
