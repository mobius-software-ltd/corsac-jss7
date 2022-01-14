package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingRequestCS1Impl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

public class ApplyChargingTest 
{
	protected final transient Logger logger=Logger.getLogger(ApplyChargingTest.class);

	private byte[] message1=new byte[] { 0x30,0x0f,(byte)0xa0,0x0a,(byte)0xa0,0x04,(byte)0x82,0x02,0x00,
			(byte)0xf0,(byte)0xa1,0x02,(byte)0x80,0x00,(byte)0x81,0x01,(byte)0xff };
	
	private byte[] message2=new byte[] { 0x30,0x0d,(byte)0xa0,0x08,(byte)0xa0,0x02,(byte)0x81,0x00,
			(byte)0xa1,0x02,(byte)0x80,0x00,(byte)0x81,0x01,(byte)0xff };

	private byte[] message3=new byte[] { 0x30,0x0e,(byte)0xa0,0x09,(byte)0xa0,0x03,(byte)0x82,0x01,0x20,
			(byte)0xa1,0x02,(byte)0x80,0x00,(byte)0x81,0x01,(byte)0xff };

	private byte[] message4=new byte[] { 0x30,0x0f,(byte)0xa0,0x0a,(byte)0xa0,0x04,(byte)0x82,0x02,0x0b,
			0x40,(byte)0xa1,0x02,(byte)0x80,0x00,(byte)0x81,0x01,(byte)0xff };
	
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(ApplyChargingRequestCS1Impl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingRequestCS1Impl);
	        
		ApplyChargingRequestCS1Impl elem = (ApplyChargingRequestCS1Impl)result.getResult();
		logger.info(elem);	
		
		rawData = this.message2;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingRequestCS1Impl);
	        
		elem = (ApplyChargingRequestCS1Impl)result.getResult();
		logger.info(elem);	
		
		rawData = this.message3;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingRequestCS1Impl);
	        
		elem = (ApplyChargingRequestCS1Impl)result.getResult();
		logger.info(elem);	
		
		rawData = this.message4;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingRequestCS1Impl);
	        
		elem = (ApplyChargingRequestCS1Impl)result.getResult();
		logger.info(elem);	
	}
}