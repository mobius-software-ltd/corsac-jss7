package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
*
* @author kostiantyn nosach
*
*/

public class SipUriImpl extends ASNOctetString implements SipUri {
	
	public SipUriImpl() {
    }

    public SipUriImpl(ByteBuf value) {
        super(value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ISDNSubaddressStringImpl");
        sb.append(" [");
        if (getValue()!=null) {
            sb.append("data=");
            sb.append(printDataArr());            
        }
        sb.append("]");

        return sb.toString();
    }
}