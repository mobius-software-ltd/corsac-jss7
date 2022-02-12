package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNPrivacyCheckRelatedAction extends ASNEnumerated {
	public ASNPrivacyCheckRelatedAction() {
		super("PrivacyCheckRelatedAction",0,4,false);
	}
	
	public ASNPrivacyCheckRelatedAction(PrivacyCheckRelatedAction t) {
		super(t.getAction(),"PrivacyCheckRelatedAction",0,4,false);
	}
	
	public PrivacyCheckRelatedAction getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(realValue);
	}
}
