package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNISTSupportIndicatorImpl extends ASNEnumerated {
	public void setType(ISTSupportIndicator t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ISTSupportIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ISTSupportIndicator.getInstance(getValue().intValue());
	}
}
