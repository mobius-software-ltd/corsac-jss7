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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CallResultCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CallResultReportCondition;
import org.restcomm.protocols.ss7.inap.primitives.DateAndTimeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingReportRequestCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CallResultCS1Impl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class ApplyChargingReportTest 
{
	protected final transient Logger logger=LogManager.getLogger(ApplyChargingReportTest.class);

	private byte[] message1=new byte[] {  0x04,0x13,(byte)0x80,0x01,0x01,(byte)0x81,0x06,0x12,(byte)0x90,
			0x42,(byte)0x80,0x52,0x12,(byte)0xa2,0x03,(byte)0x81,0x01,0x01,(byte)0x83,0x01,0x04
	};
	 
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingReportRequestCS1Impl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingReportRequestCS1Impl);
	        
		ApplyChargingReportRequestCS1Impl elem = (ApplyChargingReportRequestCS1Impl)result.getResult();
		assertNotNull(elem.getCallResultCS1());
		assertEquals(elem.getCallResultCS1().getCallResultReportCondition(),CallResultReportCondition.endOfConnection);
		assertNotNull(elem.getCallResultCS1().getTimeStamp());
		assertNotNull(elem.getCallResultCS1().getPartyToCharge());
		assertEquals(elem.getCallResultCS1().getPartyToCharge(),LegType.leg1);
		assertEquals(elem.getCallResultCS1().getAccumulatedCharge(),new Integer(4));
		logger.info(elem);		
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingReportRequestImpl.class);
	    	
		DateAndTime timeStamp=new DateAndTimeImpl(21, 9, 24, 8, 25, 21);
		CallResultCS1 callResult=new CallResultCS1Impl(CallResultReportCondition.endOfConnection, timeStamp, LegType.leg1, 4, null, null, null);
		ApplyChargingReportRequestCS1Impl elem = new ApplyChargingReportRequestCS1Impl(callResult);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}