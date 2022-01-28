package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNReportAmountImpl extends ASNEnumerated {
	public ASNReportAmountImpl() {
	}
	
	public ASNReportAmountImpl(ReportAmount t) {
		super(t.getCode());
	}
	
	public ReportAmount getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ReportAmount.getInstance(realValue);
	}
}
