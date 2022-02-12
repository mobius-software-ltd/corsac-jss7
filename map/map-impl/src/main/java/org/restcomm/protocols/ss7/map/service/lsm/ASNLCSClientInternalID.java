package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSClientInternalID extends ASNEnumerated {
	public ASNLCSClientInternalID() {
		super("LCSClientInternalID",0,4,false);
	}
	
	public ASNLCSClientInternalID(LCSClientInternalID t) {
		super(t.getId(),"LCSClientInternalID",0,4,false);
	}
	
	public LCSClientInternalID getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LCSClientInternalID.getLCSClientInternalID(realValue);
	}
}