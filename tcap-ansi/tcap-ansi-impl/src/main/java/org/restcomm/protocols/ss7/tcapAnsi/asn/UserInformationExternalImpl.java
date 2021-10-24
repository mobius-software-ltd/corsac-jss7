package org.restcomm.protocols.ss7.tcapAnsi.asn;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.common.ExternalImpl;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0x08,constructed=true,lengthIndefinite=false)
public class UserInformationExternalImpl extends ExternalImpl<ASNUserInformationObjectImpl> {
	public UserInformationExternalImpl() {
		
	}
}