package org.restcomm.protocols.ss7.inap.cs1plus;
/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RequestedReportInfo;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingRequestCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ReportConditionImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.RequestedReportInfoImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class ApplyChargingTest 
{
	protected final transient Logger logger=LogManager.getLogger(ApplyChargingTest.class);

	private byte[] message1=new byte[] { 0x30,0x0d,(byte)0xa0,0x08,(byte)0xa0,0x02,(byte)0x81,
			0x00,(byte)0xa1,0x02,(byte)0x80,0x00,(byte)0x81,0x01,(byte)0xff	};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingRequestCS1Impl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingRequestCS1Impl);
	        
		ApplyChargingRequestCS1Impl elem = (ApplyChargingRequestCS1Impl)result.getResult();
		assertNotNull(elem.getAChBillingChargingCharacteristics());
		assertNotNull(((AchBillingChargingCharacteristicsCS1Impl)elem.getAChBillingChargingCharacteristics()).getReportCondition());
		assertTrue(((AchBillingChargingCharacteristicsCS1Impl)elem.getAChBillingChargingCharacteristics()).getReportCondition().getReportAtEndOfConnection());
		assertNotNull(((AchBillingChargingCharacteristicsCS1Impl)elem.getAChBillingChargingCharacteristics()).getRequestedReportInfo());
		assertTrue(((AchBillingChargingCharacteristicsCS1Impl)elem.getAChBillingChargingCharacteristics()).getRequestedReportInfo().getAccumulatedCharge());
		assertNotNull(elem.getSendCalculationToSCPIndication());
		assertEquals(elem.getSendCalculationToSCPIndication(), new Boolean(true));	
		logger.info(elem);		
	}
	
	@Test
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingRequestCS1Impl.class);
	    	
		ReportCondition reportCondition=new ReportConditionImpl(true, false);
		RequestedReportInfo requestedReportInfo=new RequestedReportInfoImpl(true,false,false,false);
		AchBillingChargingCharacteristicsCS1 achBillingChargingCharacteristics=new AchBillingChargingCharacteristicsCS1Impl(reportCondition,requestedReportInfo);
		ApplyChargingRequestCS1Impl elem = new ApplyChargingRequestCS1Impl(achBillingChargingCharacteristics, true, null, null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}