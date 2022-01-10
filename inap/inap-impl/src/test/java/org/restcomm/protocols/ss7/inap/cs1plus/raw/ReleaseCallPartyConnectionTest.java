package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ReleaseCallPartyConnectionRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

public class ReleaseCallPartyConnectionTest 
{
	protected final transient Logger logger=Logger.getLogger(ReleaseCallPartyConnectionTest.class);

	private byte[] message1=new byte[] { 0x30,0x09,(byte)0xa0,0x03,(byte)0x80,0x01,0x01,(byte)0x82,
			0x02,(byte)0x80,(byte)0x90 };

	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(ReleaseCallPartyConnectionRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ReleaseCallPartyConnectionRequestImpl);
	        
		ReleaseCallPartyConnectionRequestImpl elem = (ReleaseCallPartyConnectionRequestImpl)result.getResult();
		logger.info(elem);			
	}
}