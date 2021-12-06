package org.restcomm.protocols.ss7.map.api.service.sms;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
<code>
SIP-URI ::= OCTET STRING
-- octets are coded as defined in IETF RFC 3261
</code>
 *
 * @author kostiantyn nosach
 */

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface SipUri {

    byte[] getData();

}