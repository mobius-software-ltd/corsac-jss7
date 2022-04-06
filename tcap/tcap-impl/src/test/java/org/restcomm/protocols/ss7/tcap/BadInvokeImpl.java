package org.restcomm.protocols.ss7.tcap;

import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
*
* @author yulian oifa
*
*/
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,lengthIndefinite=false)
public class BadInvokeImpl extends ReturnResultImpl 
{
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1)
	private ASNOctetString testString=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 0x04, 0x08 }),null,null,null,false);
}