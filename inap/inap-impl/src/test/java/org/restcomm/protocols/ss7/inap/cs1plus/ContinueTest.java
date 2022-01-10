package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingReportRequestImpl;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ContinueTest 
{
	protected final transient Logger logger=Logger.getLogger(ContinueTest.class);

	private byte[] message1=new byte[] {  0x04,0x13,(byte)0x80,0x01,0x01,(byte)0x81,0x06,0x12,(byte)0x90,
			0x42,(byte)0x80,0x52,0x12,(byte)0xa2,0x03,(byte)0x81,0x01,0x01,(byte)0x83,0x01,0x04
	};
	
	private byte[] octetData1=new byte[] { (byte)0x80,0x01,0x01,(byte)0x81,0x06,0x12,(byte)0x90,
			0x42,(byte)0x80,0x52,0x12,(byte)0xa2,0x03,(byte)0x81,0x01,0x01,(byte)0x83,0x01,0x04
	};
	 
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingReportRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingReportRequestImpl);
	        
		ApplyChargingReportRequestImpl elem = (ApplyChargingReportRequestImpl)result.getResult();
		assertNotNull(elem.getCallResult());
		assertEquals(elem.getCallResult().length,0x13);	    
		assertTrue(Arrays.equals(octetData1, elem.getCallResult()));
		logger.info(elem);		
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingReportRequestImpl.class);
	    	
	    ApplyChargingReportRequestImpl elem = new ApplyChargingReportRequestImpl(octetData1);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}