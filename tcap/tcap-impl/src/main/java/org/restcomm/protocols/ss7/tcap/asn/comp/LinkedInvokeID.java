package org.restcomm.protocols.ss7.tcap.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=true,lengthIndefinite=false)
public class LinkedInvokeID extends ASNInteger {
	public LinkedInvokeID() {
		super(null,"LinkedInvokeID",-128,127,false);
	}
	
	public LinkedInvokeID(Integer value) {
    	super(value,"LinkedInvokeID",-128,127,false);
    }
}