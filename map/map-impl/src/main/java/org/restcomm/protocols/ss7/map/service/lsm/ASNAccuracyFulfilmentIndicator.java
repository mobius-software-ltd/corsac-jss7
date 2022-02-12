package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNAccuracyFulfilmentIndicator extends ASNEnumerated {
	public ASNAccuracyFulfilmentIndicator() {
		super("AccuracyFulfilmentIndicator",0,1,false);
	}
	public ASNAccuracyFulfilmentIndicator(AccuracyFulfilmentIndicator t) {
		super(t.getIndicator(),"AccuracyFulfilmentIndicator",0,1,false);
	}
	
	public AccuracyFulfilmentIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return AccuracyFulfilmentIndicator.getAccuracyFulfilmentIndicator(realValue);
	}
}
