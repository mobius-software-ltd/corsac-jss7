package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SendChargingInformationRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

public class SendingChargingInformationTest 
{
	protected final transient Logger logger=Logger.getLogger(SendingChargingInformationTest.class);

	private byte[] message1=new byte[] {0x30,0x24,(byte)0xa0,0x1d,(byte)0xa0,0x1b,(byte)0xa1,0x19,
			(byte)0x81,0x01,0x01,(byte)0xa2,0x14,(byte)0xa0,0x12,(byte)0x80,0x01,0x01,(byte)0x81,
			0x01,0x01,(byte)0x82,0x01,0x01,(byte)0x83,0x01,0x01,(byte)0x84,0x01,0x01,(byte)0x85,
			0x01,0x03,(byte)0xa1,0x03,(byte)0x80,0x01,0x01 };
	
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(SendChargingInformationRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof SendChargingInformationRequestImpl);
	        
		SendChargingInformationRequestImpl elem = (SendChargingInformationRequestImpl)result.getResult();
		logger.info(elem);					
	}
}