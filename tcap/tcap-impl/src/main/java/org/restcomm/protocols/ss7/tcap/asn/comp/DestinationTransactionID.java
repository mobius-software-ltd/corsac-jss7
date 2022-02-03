package org.restcomm.protocols.ss7.tcap.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNPostprocess;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x09,constructed=false,lengthIndefinite=false)
@ASNPostprocess
public class DestinationTransactionID extends ASNOctetString {
	public DestinationTransactionID() {
		super("DestinationTransactionID",1,4,false);
	}
	
	public DestinationTransactionID(ByteBuf value) {
		super(value,"DestinationTransactionID",1,4,false);
	}
}