package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.NotReachableReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNNotReachableReasonImpl extends ASNEnumerated {
	public ASNNotReachableReasonImpl() {
		super("NotReachableReason",0,3,false);		
	}
	
	public ASNNotReachableReasonImpl(NotReachableReason t) {
		super(t.getCode(),"NotReachableReason",0,3,false);
	}
	
	public NotReachableReason getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return NotReachableReason.getInstance(realValue);
	}
}