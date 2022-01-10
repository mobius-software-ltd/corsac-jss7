package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.cs1plus.ApplyChargingReportTest;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

public class RequestReportBCSMEventTest {
	protected final transient Logger logger=Logger.getLogger(ApplyChargingReportTest.class);

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
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
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
