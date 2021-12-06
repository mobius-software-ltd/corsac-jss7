package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNReportAmountImpl extends ASNEnumerated {
	public void setType(ReportAmount t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ReportAmount getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ReportAmount.getInstance(getValue().intValue());
	}
}
