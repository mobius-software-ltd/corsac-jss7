package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SendChargingInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.SCIBillingChargingCharacteristicsImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SendChargingInformationTest 
{
	protected final transient Logger logger=Logger.getLogger(SendChargingInformationTest.class);

	private byte[] message1=new byte[] {0x30,0x24,(byte)0xa0,0x1d,(byte)0xa0,0x1b,(byte)0xa1,0x19,
			(byte)0x81,0x01,0x01,(byte)0xa2,0x14,(byte)0xa0,0x12,(byte)0x80,0x01,0x01,(byte)0x81,
			0x01,0x01,(byte)0x82,0x01,0x01,(byte)0x83,0x01,0x01,(byte)0x84,0x01,0x01,(byte)0x85,
			0x01,0x03,(byte)0xa1,0x03,(byte)0x80,0x01,0x01
	};
	
	private byte[] sciBillingChargingCharacteristicsData1=new byte[] { -96, 27, -95, 25, -127, 1, 1, -94, 20, -96, 18, -128, 1, 1, 
			-127, 1, 1, -126, 1, 1, -125, 1, 1, -124, 1, 1, -123, 1, 3 };
	 
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
		assertNotNull(elem.getPartyToCharge());
		assertEquals(elem.getPartyToCharge(),LegType.leg1);
		assertNotNull(elem.getSCIBillingChargingCharacteristics());
		assertTrue(Arrays.equals(sciBillingChargingCharacteristicsData1, elem.getSCIBillingChargingCharacteristics().getData()));
		logger.info(elem);		
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(SendChargingInformationRequestImpl.class);
	    	
		SCIBillingChargingCharacteristics sciBillingChargingCharacteristics=new SCIBillingChargingCharacteristicsImpl(sciBillingChargingCharacteristicsData1);
		SendChargingInformationRequestImpl elem = new SendChargingInformationRequestImpl(sciBillingChargingCharacteristics,LegType.leg1, null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}