package org.restcomm.protocols.ss7.map.primitives;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;
/**
 * @author yulian oifa
 *
 */
public class TestOctetStringLength1Impl extends ASNSingleByte {
	public TestOctetStringLength1Impl(int data) {
        setValue(data);
    }

    public TestOctetStringLength1Impl() {            
    }

    public int getData() {
        return this.getValue();
    }
}
