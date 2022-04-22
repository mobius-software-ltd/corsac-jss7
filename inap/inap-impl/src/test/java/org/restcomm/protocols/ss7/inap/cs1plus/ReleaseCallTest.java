package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ReleaseCallRequestImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class ReleaseCallTest 
{
	protected final transient Logger logger=LogManager.getLogger(ReleaseCallTest.class);

	private byte[] message1=new byte[] {  0x04,0x02,(byte)0x80,(byte)0xcb};
	
	private byte[] causeData1=new byte[] { -128, -53 };
	 
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ReleaseCallRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ReleaseCallRequestImpl);
	        
		ReleaseCallRequestImpl elem = (ReleaseCallRequestImpl)result.getResult();
		assertNotNull(elem.getCause());
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(causeData1),CauseIsupImpl.translate(elem.getCause().getCauseIndicators())));
		assertEquals(elem.getCause().getCauseIndicators().getCodingStandard(),0);	    
		assertEquals(elem.getCause().getCauseIndicators().getLocation(),0);	    
		assertEquals(elem.getCause().getCauseIndicators().getRecommendation(),0);	    
		assertEquals(elem.getCause().getCauseIndicators().getCauseValue(),75);	    
		logger.info(elem);		
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ReleaseCallRequestImpl.class);
	    	
		CauseIndicatorsImpl ci=new CauseIndicatorsImpl();
		ci.decode(Unpooled.wrappedBuffer(causeData1));
		CauseIsup cause=new CauseIsupImpl(ci);
		ReleaseCallRequestImpl elem = new ReleaseCallRequestImpl(cause);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}