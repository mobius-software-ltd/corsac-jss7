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
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SendChargingInformationCS1RequestImpl;
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
public class SendingChargingInformationTest 
{
	protected final transient Logger logger=LogManager.getLogger(SendingChargingInformationTest.class);

	private byte[] message1=new byte[] {0x30,0x24,(byte)0xa0,0x1d,(byte)0xa0,0x1b,(byte)0xa1,0x19,
			(byte)0x81,0x01,0x01,(byte)0xa2,0x14,(byte)0xa0,0x12,(byte)0x80,0x01,0x01,(byte)0x81,
			0x01,0x01,(byte)0x82,0x01,0x01,(byte)0x83,0x01,0x01,(byte)0x84,0x01,0x01,(byte)0x85,
			0x01,0x03,(byte)0xa1,0x03,(byte)0x80,0x01,0x01 };
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(SendChargingInformationCS1RequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof SendChargingInformationCS1RequestImpl);
	        
		SendChargingInformationCS1RequestImpl elem = (SendChargingInformationCS1RequestImpl)result.getResult();
		logger.info(elem);					
	}
}