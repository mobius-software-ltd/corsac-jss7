package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ReleaseCallPartyConnectionRequestImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class ReleaseCallPartyConnectionTest 
{
	protected final transient Logger logger=Logger.getLogger(ReleaseCallPartyConnectionTest.class);

	private byte[] message1=new byte[] { 0x30,0x09,(byte)0xa0,0x03,(byte)0x80,0x01,0x01,(byte)0x82,
			0x02,(byte)0x82,(byte)0x90 };
	
	private byte[] causeData1=new byte[] { -126, -112 };
	 
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ReleaseCallPartyConnectionRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ReleaseCallPartyConnectionRequestImpl);
	        
		ReleaseCallPartyConnectionRequestImpl elem = (ReleaseCallPartyConnectionRequestImpl)result.getResult();
		assertNotNull(elem.getReleaseCause());
		assertTrue(ByteBufUtil.equals(CauseIsupImpl.translate(elem.getReleaseCause().getCauseIndicators()),Unpooled.wrappedBuffer(causeData1)));
		assertEquals(elem.getReleaseCause().getCauseIndicators().getCodingStandard(), 0);
		assertEquals(elem.getReleaseCause().getCauseIndicators().getLocation(), 2);
		assertEquals(elem.getReleaseCause().getCauseIndicators().getRecommendation(), 0);
		assertEquals(elem.getReleaseCause().getCauseIndicators().getCauseValue(), 16);
		assertNotNull(elem.getLegToBeReleased());
		assertEquals(elem.getLegToBeReleased(), LegType.leg1);
		
		logger.info(elem);
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ReleaseCallPartyConnectionRequestImpl.class);
	    	
		CauseIndicatorsImpl ci=new CauseIndicatorsImpl();
		ci.decode(Unpooled.wrappedBuffer(causeData1));
		CauseIsup causeIsup=new CauseIsupImpl(ci);
		ReleaseCallPartyConnectionRequestImpl elem = new ReleaseCallPartyConnectionRequestImpl(LegType.leg1,null,causeIsup);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}