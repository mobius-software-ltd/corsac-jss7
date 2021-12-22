package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
<code>
CorrelationID ::= SEQUENCE
{
    hlr-id [0] HLR-Id OPTIONAL,
    sip-uri-A [1] SIP-URI OPTIONAL,
    sip-uri-B [2] SIP-URI OPTIONAL
}
HLR-Id ::= IMSI
SIP-URI ::= OCTET STRING
-- octets are coded as defined in IETF RFC 3261
</code>
 *
 * @author kostiantyn nosach
 */

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface CorrelationID {

    IMSI getHlrId();

    SipUri getSipUriA();

    SipUri getSipUriB();
}