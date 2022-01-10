package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.AChBillingChargingCharacteristicsImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ApplyChargingTest 
{
	protected final transient Logger logger=Logger.getLogger(ApplyChargingTest.class);

	private byte[] message1=new byte[] { 0x30,0x0d,(byte)0xa0,0x08,(byte)0xa0,0x02,(byte)0x81,
			0x00,(byte)0xa1,0x02,(byte)0x80,0x00,(byte)0x81,0x01,(byte)0xff	};
	
	private byte[] aChBillingChargingCharacteristicsData=new byte[] { -96, 2, -127, 0, -95, 2, -128, 0 };
	 
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingRequestImpl);
	        
		ApplyChargingRequestImpl elem = (ApplyChargingRequestImpl)result.getResult();
		assertNotNull(elem.getAChBillingChargingCharacteristics());
		assertTrue(Arrays.equals(aChBillingChargingCharacteristicsData, elem.getAChBillingChargingCharacteristics().getData()));
		assertNotNull(elem.getSendCalculationToSCPIndication());
		assertEquals(elem.getSendCalculationToSCPIndication(), new Boolean(true));	
		logger.info(elem);		
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ApplyChargingRequestImpl.class);
	    	
		AChBillingChargingCharacteristics achBillingChargingCharacteristics=new AChBillingChargingCharacteristicsImpl(aChBillingChargingCharacteristicsData);
		ApplyChargingRequestImpl elem = new ApplyChargingRequestImpl(achBillingChargingCharacteristics, true, null, null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}