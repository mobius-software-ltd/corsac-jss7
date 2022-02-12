package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTimerID extends ASNEnumerated {
	public ASNTimerID() {
		super("TimerID",0,0,false);
	}
	
	public ASNTimerID(TimerID t) {
		super(t.getCode(),"TimerID",0,0,false);
	}
	
	public TimerID getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return TimerID.getInstance(realValue);
	}
}