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
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ConnectRequestImpl;
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
public class ConnectTest 
{
	protected final transient Logger logger=LogManager.getLogger(ConnectTest.class);

	private byte[] message1=new byte[] { 0x30,0x0c,(byte)0xa0,0x0a,0x04,0x08,(byte)0x84,(byte)0x90,
			(byte)0x81,0x67,0x73,0x11,0x34,0x07 };
	
	private byte[] message2=new byte[] { 0x30,0x0e,(byte)0xa0,0x0c,0x04,0x0a,0x02,(byte)0x90,0x10,
			0x51,0x52,0x55,0x55,(byte)0x86,0x78,0x22
	};
	
	private byte[] message3=new byte[] { 0x30,0x0c,(byte)0xa0,0x0a,0x04,0x08,(byte)0x84,(byte)0x90,
			(byte)0x81,0x67,0x74,0x72,0x59,0x02 };
	
	private byte[] message4=new byte[] { 0x30,0x0c,(byte)0xa0,0x0a,0x04,0x08,(byte)0x84,(byte)0x90,
			(byte)0x81,0x67,(byte)0x82,0x42,0x10,0x08 };
	
	private byte[] message5=new byte[] { 0x30,0x0c,(byte)0xa0,0x0a,0x04,0x08,(byte)0x84,(byte)0x90,
			(byte)0x81,0x67,0x18,0x10,(byte)0x80,0x02};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(ConnectRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectRequestImpl);
	        
		ConnectRequestImpl elem = (ConnectRequestImpl)result.getResult();
		logger.info(elem);	
		
		rawData = this.message2;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectRequestImpl);
	        
		elem = (ConnectRequestImpl)result.getResult();
		logger.info(elem);	
		
		rawData = this.message3;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectRequestImpl);
	        
		elem = (ConnectRequestImpl)result.getResult();
		logger.info(elem);
		
		rawData = this.message4;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectRequestImpl);
	        
		elem = (ConnectRequestImpl)result.getResult();
		logger.info(elem);
		
		rawData = this.message5;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectRequestImpl);
	        
		elem = (ConnectRequestImpl)result.getResult();
		logger.info(elem);
	}
}