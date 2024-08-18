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
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.InitialDPRequestImpl;
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
public class InitialDPTest 
{
	protected final transient Logger logger=LogManager.getLogger(InitialDPTest.class);

	private byte[] message1=new byte[] { 0x30,0x4f,(byte)0x80,0x01,0x4b,(byte)0x82,0x09,0x02,(byte)0x90,0x10,
			0x11,0x34,0x27,0x32,0x00,0x50,(byte)0x83,0x07,0x03,0x13,0x78,0x46,(byte)0x98,0x02,(byte)0x92,(byte)0x8a,
			0x08,(byte)0x84,(byte)0x93,(byte)0x81,0x67,(byte)0x83,0x50,0x51,0x00,(byte)0xaf,0x22,0x30,0x10,0x02,0x01,0x23,
			(byte)0xa1,0x0b,0x04,0x09,0x00,0x00,0x33,(byte)0xf8,0x50,0x08,(byte)0xfc,0x32,(byte)0x91,0x30,0x0e,0x02,
			0x01,0x2f,(byte)0xa1,0x09,0x04,0x07,0x00,0x00,(byte)0xaf,(byte)0xe2,0x01,0x00,0x0d,(byte)0xbb,0x05,(byte)0x80,
			0x03,(byte)0x80,(byte)0x90,(byte)0xa2,(byte)0x9c,0x01,0x03 };

	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(InitialDPRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof InitialDPRequestImpl);
	        
		InitialDPRequestImpl elem = (InitialDPRequestImpl)result.getResult();
		logger.info(elem);			
	}
}