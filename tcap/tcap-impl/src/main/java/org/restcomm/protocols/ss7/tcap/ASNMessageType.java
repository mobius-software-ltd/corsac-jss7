package org.restcomm.protocols.ss7.tcap;

import org.restcomm.protocols.ss7.tcap.api.MessageType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

public class ASNMessageType extends ASNInteger {
	public void setType(MessageType t) {
		super.setValue(Long.valueOf(t.getValue()));
	}
	
	public MessageType getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return MessageType.getValue(getValue().intValue());
	}
}