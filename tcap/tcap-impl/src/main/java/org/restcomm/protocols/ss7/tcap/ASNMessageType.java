package org.restcomm.protocols.ss7.tcap;

import org.restcomm.protocols.ss7.tcap.api.MessageType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNMessageType extends ASNEnumerated {
	public ASNMessageType() {
		
	}
	
	public ASNMessageType(MessageType t) {
		super(t.getValue());
	}
	
	public MessageType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return MessageType.getValue(realValue);
	}
}