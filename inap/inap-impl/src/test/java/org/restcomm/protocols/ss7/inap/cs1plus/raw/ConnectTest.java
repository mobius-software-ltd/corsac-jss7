package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ConnectRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

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
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
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