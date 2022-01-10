package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportCS1PlusRequest;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SpecializedResourceReportCS1PlusRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SpecializedResourceReportTest 
{
	protected final transient Logger logger=Logger.getLogger(SpecializedResourceReportTest.class);

	private byte[] message1=new byte[] { 0x05,0x00 };
	 
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(SpecializedResourceReportCS1PlusRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof SpecializedResourceReportCS1PlusRequestImpl);
	    
		SpecializedResourceReportCS1PlusRequestImpl elem = (SpecializedResourceReportCS1PlusRequestImpl)result.getResult();
		assertTrue(elem.getAnnouncementCompleted());	     	
		assertFalse(elem.getAnnouncementStarted());
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(SpecializedResourceReportCS1PlusRequestImpl.class);
	    	
		SpecializedResourceReportCS1PlusRequest elem = new SpecializedResourceReportCS1PlusRequestImpl(true,false);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}