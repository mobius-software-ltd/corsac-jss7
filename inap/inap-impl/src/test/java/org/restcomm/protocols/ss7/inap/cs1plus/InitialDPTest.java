package org.restcomm.protocols.ss7.inap.cs1plus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ExtensionField;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.isup.BearerIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ExtensionFieldImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.InitialDPRequestImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class InitialDPTest {
	protected final transient Logger logger=LogManager.getLogger(ApplyChargingReportTest.class);

	private byte[] message1=new byte[] {  0x30,0x50,(byte)0x80,0x01,0x0a,(byte)0x82,0x0a,0x02,(byte)0x90,
			0x10,0x51,0x52,0x55,0x55,(byte)0x86,0x78,0x22,(byte)0x83,0x07,0x03,0x13,0x78,(byte)0x86,
			0x56,0x38,(byte)0x86,(byte)0x8a,0x08,(byte)0x84,(byte)0x93,(byte)0x81,0x67,(byte)0x83,0x50,
			0x51,0x00,(byte)0xaf,0x22,0x30,0x10,0x02,0x01,0x23,(byte)0xa1,0x0b,0x04,0x09,0x00,0x00,
			0x33,(byte)0xf8,0x50,0x08,(byte)0xfc,0x36,0x1d,0x30,0x0e,0x02,0x01,0x2f,(byte)0xa1,0x09,
			0x04,0x07,0x00,0x00,0x2d,(byte)0xd0,(byte)0xd8,0x00,0x0b,(byte)0xbb,0x05,(byte)0x80,0x03,
			(byte)0x80,(byte)0x90,(byte)0xa2,(byte)0x9c,0x01,0x03
	};
	
	private byte[] calledPartyData=new byte[] { 2, -112, 16, 81, 82, 85, 85, -122, 120, 34};
	private byte[] callingPartyData=new byte[] { 3, 19, 120, -122, 86, 56, -122};
	private byte[] locationNumberData=new byte[] { -124, -109, -127, 103, -125, 80, 81, 0};
	
	private byte[] extension1Data=new byte[] { 4, 9, 0, 0, 51, -8, 80, 8, -4, 54, 29};
	private byte[] extension2Data=new byte[] { 4, 7, 0, 0, 45, -48, -40, 0, 11};
	
	private byte[] bearerData=new byte[] { -128, -112, -94};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(InitialDPRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof InitialDPRequestImpl);
	        
		InitialDPRequestImpl elem = (InitialDPRequestImpl)result.getResult();
		assertEquals(elem.getServiceKey(),10);
		
		assertNotNull(elem.getCalledPartyNumber());
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(calledPartyData),CalledPartyNumberIsupImpl.translate(elem.getCalledPartyNumber().getCalledPartyNumber())));
		assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getNumberingPlanIndicator(), 1);
		assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getInternalNetworkNumberIndicator(), 1);
		assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getNatureOfAddressIndicator(), 2);
		assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getAddress(), "0115255555688722");
		
		assertNotNull(elem.getCallingPartyNumber());
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(callingPartyData),CallingPartyNumberIsupImpl.translate(elem.getCallingPartyNumber().getCallingPartyNumber())));
		assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getNumberingPlanIndicator(), 1);
		assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getNumberIncompleteIndicator(), 0);
		assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getAddressRepresentationRestrictedIndicator(), 0);
		assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getScreeningIndicator(), 3);
		assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getNatureOfAddressIndicator(), 3);
		assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getAddress(), "8768658368");
		
		assertNotNull(elem.getLocationNumber());
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(locationNumberData), LocationNumberIsupImpl.translate(elem.getLocationNumber().getLocationNumber())));
		assertEquals(elem.getLocationNumber().getLocationNumber().getNumberingPlanIndicator(), 1);
		assertEquals(elem.getLocationNumber().getLocationNumber().getInternalNetworkNumberIndicator(), 1);
		assertEquals(elem.getLocationNumber().getLocationNumber().getAddressRepresentationRestrictedIndicator(), 0);
		assertEquals(elem.getLocationNumber().getLocationNumber().getScreeningIndicator(), 3);
		assertEquals(elem.getLocationNumber().getLocationNumber().getNatureOfAddressIndicator(), 4);
		assertEquals(elem.getLocationNumber().getLocationNumber().getAddress(), "18763805150");
		
		assertNotNull(elem.getExtensions());
		assertNotNull(elem.getExtensions().getExtensionFields());
		assertEquals(elem.getExtensions().getExtensionFields().size(), 2);
		assertNotNull(elem.getExtensions().getExtensionFields().get(0).getLocalCode());
		assertEquals(elem.getExtensions().getExtensionFields().get(0).getLocalCode(), new Integer(35));
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(extension1Data), elem.getExtensions().getExtensionFields().get(0).getValue()));
		assertNotNull(elem.getExtensions().getExtensionFields().get(1).getLocalCode());
		assertEquals(elem.getExtensions().getExtensionFields().get(1).getLocalCode(), new Integer(47));
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(extension2Data), elem.getExtensions().getExtensionFields().get(1).getValue()));
		
		assertNotNull(elem.getBearerCapability());
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(bearerData),BearerIsupImpl.translate(elem.getBearerCapability().getBearerCap().getUserServiceInformation())));
		assertNotNull(elem.getBearerCapability().getBearerCap().getUserServiceInformation());
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getAssignor(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getCodingStandart(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getCustomInformationTransferRate(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getDataBits(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getDuplexMode(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getFlowControlOnRx(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getFlowControlOnTx(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getHDR(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getInBandNegotiation(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getInformationTransferCapability(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getInformationTransferRate(), 16);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getIntermediateRate(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getL1UserInformation(), 2);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getL2UserInformation(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getL3UserInformation(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getL3Protocol(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getLLINegotiation(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getMode(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getModemType(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getMultiframe(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getNegotiation(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getNicOnRx(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getNicOnTx(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getParity(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getStopBits(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getSyncMode(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getTransferMode(), 0);
		assertEquals(elem.getBearerCapability().getBearerCap().getUserServiceInformation().getUserRate(), 0);
				
		assertNotNull(elem.getEventTypeBCSM());
		assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.analyzedInformation);
		
		logger.info(elem);
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(InitialDPRequestImpl.class);
	    	
		CalledPartyNumberIsup calledPartyNumber=new CalledPartyNumberIsupImpl(new CalledPartyNumberImpl(Unpooled.wrappedBuffer(calledPartyData)));
		CallingPartyNumberIsup callingPartyNumber=new CallingPartyNumberIsupImpl(new CallingPartyNumberImpl(Unpooled.wrappedBuffer(callingPartyData)));
		LocationNumberIsup locationNumber=new LocationNumberIsupImpl(new LocationNumberImpl(Unpooled.wrappedBuffer(locationNumberData)));
		
		List<ExtensionField> fieldsList=new ArrayList<ExtensionField>();
		fieldsList.add(new ExtensionFieldImpl(35, null, Unpooled.wrappedBuffer(extension1Data), true));
		fieldsList.add(new ExtensionFieldImpl(47, null, Unpooled.wrappedBuffer(extension2Data), true));
		CAPINAPExtensions extensions=new CAPINAPExtensionsImpl(fieldsList);
		
		BearerCapability bearerCapability=new BearerCapabilityImpl(new BearerIsupImpl(new UserServiceInformationImpl(Unpooled.wrappedBuffer(bearerData))));
		
		InitialDPRequestImpl elem = new InitialDPRequestImpl(10, null, calledPartyNumber,callingPartyNumber, 
				null,null,null,null,null,null,locationNumber,null, null, null,null,extensions,null,null,null, 
	            null,null, bearerCapability, EventTypeBCSM.analyzedInformation, null,null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}
