package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingReportRequestCS1Impl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

public class ApplyChargingReportTest 
{
	protected final transient Logger logger=LogManager.getLogger(ApplyChargingReportTest.class);

	private byte[] message1=new byte[] { 0x04,0x13,(byte)0x80,0x01,0x01,(byte)0x81,0x06,0x12,
			(byte)0x90,0x42,(byte)0x80,0x52,(byte)0x93,(byte)0xa2,0x03,(byte)0x81,0x01,0x01,
			(byte)0x83,0x01,0x07 };

	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(ApplyChargingReportRequestCS1Impl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ApplyChargingReportRequestCS1Impl);
	        
		ApplyChargingReportRequestCS1Impl elem = (ApplyChargingReportRequestCS1Impl)result.getResult();
		logger.info(elem);			
	}
}