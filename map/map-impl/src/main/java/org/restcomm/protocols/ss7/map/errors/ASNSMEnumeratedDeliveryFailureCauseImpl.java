package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMEnumeratedDeliveryFailureCauseImpl extends ASNEnumerated {
	public ASNSMEnumeratedDeliveryFailureCauseImpl() {
		super("SMEnumeratedDeliveryFailureCause",0,6,false);
	}
	
	public ASNSMEnumeratedDeliveryFailureCauseImpl(SMEnumeratedDeliveryFailureCause t) {
		super(t.getCode(),"SMEnumeratedDeliveryFailureCause",0,6,false);
	}
	
	public SMEnumeratedDeliveryFailureCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SMEnumeratedDeliveryFailureCause.getInstance(realValue);
	}
}