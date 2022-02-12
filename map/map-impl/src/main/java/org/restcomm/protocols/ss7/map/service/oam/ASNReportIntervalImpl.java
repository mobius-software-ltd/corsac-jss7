package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.ReportInterval;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNReportIntervalImpl extends ASNEnumerated {
	public ASNReportIntervalImpl() {
		super("ReportInterval",0,27,false);
	}
	
	public ASNReportIntervalImpl(ReportInterval t) {
		super(t.getCode(),"ReportInterval",0,27,false);
	}
	
	public ReportInterval getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ReportInterval.getInstance(realValue);
	}
}