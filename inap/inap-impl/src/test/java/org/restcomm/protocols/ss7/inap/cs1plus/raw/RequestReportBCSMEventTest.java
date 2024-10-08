package org.restcomm.protocols.ss7.inap.cs1plus.raw;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.inap.cs1plus.ApplyChargingReportTest;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class RequestReportBCSMEventTest {
	protected final transient Logger logger=LogManager.getLogger(ApplyChargingReportTest.class);

	private byte[] message1=new byte[] { 0x30,0x17,(byte)0xa0,0x15,0x30,0x06,(byte)0x80,0x01,0x0a,
			(byte)0x81,0x01,0x00,0x30,0x0b,(byte)0x80,0x01,0x09,(byte)0x81,0x01,0x00,(byte)0xa2,
			0x03,(byte)0x80,0x01,0x01 };
	
	private byte[] message2=new byte[] {0x30,0x3c,(byte)0xa0,0x3a,0x30,0x06,(byte)0x80,0x01,0x04,
			(byte)0x81,0x01,0x00,0x30,0x06,(byte)0x80,0x01,0x05,(byte)0x81,0x01,0x00,0x30,0x06,
			(byte)0x80,0x01,0x07,(byte)0x81,0x01,0x00,0x30,0x06,(byte)0x80,0x01,(byte)0xfe,
			(byte)0x81,0x01,0x00,0x30,0x0b,(byte)0x80,0x01,0x09,(byte)0x81,0x01,0x00,(byte)0xa2,
			0x03,(byte)0x80,0x01,0x02,0x30,0x0b,(byte)0x80,0x01,0x06,(byte)0x81,0x01,0x00,
			(byte)0xbe,0x03,(byte)0x81,0x01,0x22
	};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(RequestReportBCSMEventRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof RequestReportBCSMEventRequestImpl);
	        
		RequestReportBCSMEventRequestImpl elem = (RequestReportBCSMEventRequestImpl)result.getResult();
		logger.info(elem);
		
		rawData = this.message2;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof RequestReportBCSMEventRequestImpl);
	        
		elem = (RequestReportBCSMEventRequestImpl)result.getResult();
		logger.info(elem);				
	}
}
