package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author kostiantyn nosach
*
*/

public class SipUriImpl extends ASNOctetString implements SipUri {
	public byte[] getData() {
		ByteBuf value=getValue();
		if(value==null)
			return null;
		
		byte[] data=new byte[value.readableBytes()];
		value.readBytes(data);
        return data;
    }

    public SipUriImpl() {
    }

    public SipUriImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }
}