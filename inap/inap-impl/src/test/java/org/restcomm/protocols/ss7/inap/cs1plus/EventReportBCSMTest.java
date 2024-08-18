package org.restcomm.protocols.ss7.inap.cs1plus;
/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.DisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.DisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.EventReportBCSMRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class EventReportBCSMTest {
	protected final transient Logger logger=LogManager.getLogger(ApplyChargingReportTest.class);

	private byte[] message1=new byte[] {  0x30,0x15,(byte)0x80,0x01,0x09,(byte)0xa2,0x06,(byte)0xa7,
			0x04,(byte)0x80,0x02,(byte)0x80,(byte)0x90,(byte)0xa3,0x03,(byte)0x81,0x01,0x01,(byte)0xa4,0x03,(byte)0x80,0x01,0x00 };
	 
	private byte[] causeInd1=new byte[] {(byte)-128, (byte)-112};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(EventReportBCSMRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
	        
		EventReportBCSMRequestImpl elem = (EventReportBCSMRequestImpl)result.getResult();
		assertEquals(elem.getEventTypeBCSM(),EventTypeBCSM.oDisconnect);
		assertNotNull(elem.getEventSpecificInformationBCSM());
		assertNotNull(elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo());
		assertNotNull(elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause());
		assertTrue(ByteBufUtil.equals(CauseIsupImpl.translate(elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators()),Unpooled.wrappedBuffer(causeInd1)));
		assertEquals(elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue(),16);
		assertEquals(elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCodingStandard(),0);
		assertEquals(elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getLocation(),0);
		assertEquals(elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getLocation(),0);
		assertNotNull(elem.getLegID());
		assertNotNull(elem.getLegID().getReceivingSideID());
		assertEquals(elem.getLegID().getReceivingSideID(),LegType.leg1);
		assertNotNull(elem.getMiscCallInfo());
		assertEquals(elem.getMiscCallInfo().getMessageType(),MiscCallInfoMessageType.request);
		assertNull(elem.getMiscCallInfo().getDpAssignment());		
		logger.info(elem);		
	}
	
	@Test
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(EventReportBCSMRequestImpl.class);
	    	
		CauseIndicatorsImpl ci=new CauseIndicatorsImpl();
		ci.decode(Unpooled.wrappedBuffer(causeInd1));
		CauseIsup cause=new CauseIsupImpl(ci);
		DisconnectSpecificInfo oDisconnectInfo=new DisconnectSpecificInfoImpl(cause,null);
		EventSpecificInformationBCSM eventInformation=new EventSpecificInformationBCSMImpl(oDisconnectInfo,false);
		LegID legID=new LegIDImpl(LegType.leg1, null);
		MiscCallInfo miscCallInfo=new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);
		EventReportBCSMRequestImpl elem = new EventReportBCSMRequestImpl(EventTypeBCSM.oDisconnect,null,
				eventInformation,legID, miscCallInfo, null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}
