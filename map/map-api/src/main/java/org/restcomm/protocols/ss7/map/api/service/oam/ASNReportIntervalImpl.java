package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNReportIntervalImpl extends ASNEnumerated {
	public void setType(ReportInterval t) {
		super.setValue(Long.valueOf(t.getCode()));
	}
	
	public ReportInterval getType() {
		Long realValue=super.getValue();
		if(realValue==null)
			return null;
		
		return ReportInterval.getInstance(getValue().intValue());
	}
}
