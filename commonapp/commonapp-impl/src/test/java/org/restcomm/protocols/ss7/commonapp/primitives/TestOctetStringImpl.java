package org.restcomm.protocols.ss7.commonapp.primitives;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
/**
 * @author yulian oifa
 *
 */
public  class TestOctetStringImpl extends ASNOctetString2 {
	
	public TestOctetStringImpl(ByteBuf value) {
		super(value);
    }

	public TestOctetStringImpl() {
		
	}
}