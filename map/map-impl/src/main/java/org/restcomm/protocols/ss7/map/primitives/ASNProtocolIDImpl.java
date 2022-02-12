package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNProtocolIDImpl extends ASNEnumerated {
	public ASNProtocolIDImpl() {
		super("ProtocolId",1,4,false);
	}
	
	public ASNProtocolIDImpl(ProtocolId t) {
		super(t.getCode(),"ProtocolId",1,4,false);
	}
	
	public ProtocolId getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ProtocolId.getProtocolId(realValue);
	}
}
