package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ConnectToResourceRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ConnectToResourceTest 
{
	protected final transient Logger logger=Logger.getLogger(ConnectToResourceTest.class);

	private byte[] message1=new byte[] { 0x30,0x0a,(byte)0xa1,0x03,(byte)0x81,0x01,0x01,(byte)0xbe,
			0x03,(byte)0x82,0x01,0x01 };
	
	private byte[] serviceIndicatorsData=new byte[] { -126, 1, 1 };
	 
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(ConnectToResourceRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectToResourceRequestImpl);
	        
		ConnectToResourceRequestImpl elem = (ConnectToResourceRequestImpl)result.getResult();
		assertNotNull(elem.getServiceInteractionIndicators());
		assertTrue(Arrays.equals(serviceIndicatorsData, elem.getServiceInteractionIndicators().getData()));
		assertNotNull(elem.getResourceAddress());
		assertNotNull(elem.getResourceAddress().getLegID());
		assertEquals(elem.getResourceAddress().getLegID(),LegType.leg1);
		logger.info(elem);		
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ConnectToResourceRequestImpl.class);
	    	
		ServiceInteractionIndicators serviceInteractionIndicators=new ServiceInteractionIndicatorsImpl(serviceIndicatorsData);
		ConnectToResourceRequestImpl elem = new ConnectToResourceRequestImpl(LegType.leg1, null,serviceInteractionIndicators);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}