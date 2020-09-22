package org.restcomm.protocols.ss7.map.api.errors;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMEnumeratedDeliveryFailureCauseImpl extends ASNEnumerated {
	public void setType(SMEnumeratedDeliveryFailureCause t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public SMEnumeratedDeliveryFailureCause getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return SMEnumeratedDeliveryFailureCause.getInstance(getValue().intValue());
	}
}
