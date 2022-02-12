package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNReportAmountImpl extends ASNEnumerated {
	public ASNReportAmountImpl() {
		super("ReportAmount",0,7,false);
	}
	
	public ASNReportAmountImpl(ReportAmount t) {
		super(t.getCode(),"ReportAmount",0,7,false);
	}
	
	public ReportAmount getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ReportAmount.getInstance(realValue);
	}
}
