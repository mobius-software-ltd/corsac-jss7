package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAppendFreeFormatData extends ASNEnumerated {
	public ASNAppendFreeFormatData() {
		super("AppendFreeFormatData",0,1,false);
	}
	
	public ASNAppendFreeFormatData(AppendFreeFormatData t) {
		super(t.getCode(),"AppendFreeFormatData",0,1,false);
	}
	
	public AppendFreeFormatData getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AppendFreeFormatData.getInstance(realValue);
	}
}
