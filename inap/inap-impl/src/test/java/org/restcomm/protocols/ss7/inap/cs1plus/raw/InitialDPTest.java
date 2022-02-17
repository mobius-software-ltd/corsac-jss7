package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.InitialDPRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

public class InitialDPTest 
{
	protected final transient Logger logger=Logger.getLogger(InitialDPTest.class);

	private byte[] message1=new byte[] { 0x30,0x4f,(byte)0x80,0x01,0x4b,(byte)0x82,0x09,0x02,(byte)0x90,0x10,
			0x11,0x34,0x27,0x32,0x00,0x50,(byte)0x83,0x07,0x03,0x13,0x78,0x46,(byte)0x98,0x02,(byte)0x92,(byte)0x8a,
			0x08,(byte)0x84,(byte)0x93,(byte)0x81,0x67,(byte)0x83,0x50,0x51,0x00,(byte)0xaf,0x22,0x30,0x10,0x02,0x01,0x23,
			(byte)0xa1,0x0b,0x04,0x09,0x00,0x00,0x33,(byte)0xf8,0x50,0x08,(byte)0xfc,0x32,(byte)0x91,0x30,0x0e,0x02,
			0x01,0x2f,(byte)0xa1,0x09,0x04,0x07,0x00,0x00,(byte)0xaf,(byte)0xe2,0x01,0x00,0x0d,(byte)0xbb,0x05,(byte)0x80,
			0x03,(byte)0x80,(byte)0x90,(byte)0xa2,(byte)0x9c,0x01,0x03 };

	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
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