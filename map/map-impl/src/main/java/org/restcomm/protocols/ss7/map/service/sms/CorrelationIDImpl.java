package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author kostiantyn nosach
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CorrelationIDImpl implements CorrelationID {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = IMSIImpl.class)
    private IMSI hlrId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = SipUriImpl.class)
    private SipUri sipUriA;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = SipUriImpl.class)
    private SipUri sipUriB;

    public CorrelationIDImpl() {
    }

    public CorrelationIDImpl(IMSI hlrId, SipUri sipUriA, SipUri sipUriB) {
        this();
        this.hlrId = hlrId;
        this.sipUriA = sipUriA;
        this.sipUriB = sipUriB;
    }

    public IMSI getHlrId() {
        return hlrId;
    }

    public SipUri getSipUriA() {
        return sipUriA;
    }

    public SipUri getSipUriB() {
        return sipUriB;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CorrelationID [");

        if(this.hlrId!=null) {
            sb.append("hlrId=");
            sb.append(this.hlrId.toString());
            sb.append(", ");
        }
        if(this.sipUriA!=null) {
            sb.append("sipUriA=");
            sb.append(this.sipUriA.toString());
            sb.append(", ");
        }
        if (this.sipUriB != null) {
            sb.append(", sipUriB=");
            sb.append(this.sipUriB.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
