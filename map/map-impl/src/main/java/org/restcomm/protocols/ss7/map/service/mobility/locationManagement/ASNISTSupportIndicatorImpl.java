package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNISTSupportIndicatorImpl extends ASNEnumerated {
	public ASNISTSupportIndicatorImpl() {
		super("ISTSupportIndicator",0,1,false);
	}
	
	public ASNISTSupportIndicatorImpl(ISTSupportIndicator t) {
		super(t.getCode(),"ISTSupportIndicator",0,1,false);
	}
	
	public ISTSupportIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ISTSupportIndicator.getInstance(realValue);
	}
}
