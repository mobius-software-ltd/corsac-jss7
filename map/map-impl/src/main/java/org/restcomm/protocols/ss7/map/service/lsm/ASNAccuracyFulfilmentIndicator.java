package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAccuracyFulfilmentIndicator extends ASNEnumerated {
	public void setType(AccuracyFulfilmentIndicator t) {
		super.setValue(Long.valueOf(t.getIndicator()));
	}
	
	public AccuracyFulfilmentIndicator getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return AccuracyFulfilmentIndicator.getAccuracyFulfilmentIndicator(getValue().intValue());
	}
}
