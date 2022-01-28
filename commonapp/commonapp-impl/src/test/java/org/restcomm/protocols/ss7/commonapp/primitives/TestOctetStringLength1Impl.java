package org.restcomm.protocols.ss7.commonapp.primitives;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;
/**
 * @author yulian oifa
 *
 */
public class TestOctetStringLength1Impl extends ASNSingleByte {
	public TestOctetStringLength1Impl(int data) {
		super(data);
    }

    public TestOctetStringLength1Impl() {            
    }

    public int getData() {
        return this.getValue();
    }
}
