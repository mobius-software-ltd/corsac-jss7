package org.restcomm.protocols.ss7.tcap.asn;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x0B,constructed=true,lengthIndefinite=false)
public interface DialogPortion {
	
	public boolean isUnidirectional();
	
	public void setUnidirectional(boolean flag);
	
	public DialogAPDU getDialogAPDU();
	
	public void setDialogAPDU(DialogAPDU dialogAPDU);
}
