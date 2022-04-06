package org.restcomm.protocols.ss7.tcap;

import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author yulian oifa
*
*/
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=true,lengthIndefinite=false)
public class BadComponentImpl extends InvokeImpl 
{	
}
