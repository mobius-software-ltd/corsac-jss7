package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author kostiantyn nosach
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CorrelationIDImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private IMSIImpl hlrId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private SipUriImpl sipUriA;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private SipUriImpl sipUriB;

    public CorrelationIDImpl() {
    }

    public CorrelationIDImpl(IMSIImpl hlrId, SipUriImpl sipUriA, SipUriImpl sipUriB) {
        this();
        this.hlrId = hlrId;
        this.sipUriA = sipUriA;
        this.sipUriB = sipUriB;
    }

    public IMSIImpl getHlrId() {
        return hlrId;
    }

    public SipUriImpl getSipUriA() {
        return sipUriA;
    }

    public SipUriImpl getSipUriB() {
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
