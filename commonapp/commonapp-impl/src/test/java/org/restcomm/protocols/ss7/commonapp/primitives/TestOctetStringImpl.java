package org.restcomm.protocols.ss7.commonapp.primitives;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
/**
 * @author yulian oifa
 *
 */
public  class TestOctetStringImpl extends ASNOctetString {
	
	public TestOctetStringImpl(ByteBuf value) {
		super(value,null,null,null,false);
    }

	public TestOctetStringImpl() {
		super(null,null,null,false);
	}
}