package org.restcomm.protocols.ss7.tcap.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x08,constructed=false,lengthIndefinite=false)
public class OriginatingTransactionID extends ASNOctetString {
	public OriginatingTransactionID() {
		super("OriginatingTransactionID",1,4,false);		
	}
	
	public OriginatingTransactionID(ByteBuf value) {
		super(value,"OriginatingTransactionID",1,4,false);
	}
}