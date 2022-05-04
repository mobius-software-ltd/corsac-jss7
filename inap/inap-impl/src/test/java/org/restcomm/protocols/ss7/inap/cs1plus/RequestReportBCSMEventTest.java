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
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.BCSMEventImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class RequestReportBCSMEventTest {
	protected final transient Logger logger=LogManager.getLogger(ApplyChargingReportTest.class);

	private byte[] message1=new byte[] { 0x30,0x3c,(byte)0xa0,0x3a,0x30,0x06,(byte)0x80,0x01,0x04,
			(byte)0x81,0x01,0x00,0x30,0x06,(byte)0x80,0x01,0x05,(byte)0x81,0x01,0x00,0x30,0x06,
			(byte)0x80,0x01,0x07,(byte)0x81,0x01,0x00,0x30,0x06,(byte)0x80,0x01,(byte)0xfe,(byte)0x81,
			0x01,0x00,0x30,0x0b,(byte)0x80,0x01,0x09,(byte)0x81,0x01,0x00,(byte)0xa2,0x03,(byte)0x80,
			0x01,0x02,0x30,0x0b,(byte)0x80,0x01,0x06,(byte)0x81,0x01,0x00,(byte)0xbe,0x03,(byte)0x81,0x01,0x22
	};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(RequestReportBCSMEventRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof RequestReportBCSMEventRequestImpl);
	        
		RequestReportBCSMEventRequestImpl elem = (RequestReportBCSMEventRequestImpl)result.getResult();
		assertNotNull(elem.getBCSMEventList());
		assertEquals(elem.getBCSMEventList().size(),6);
		
		assertEquals(elem.getBCSMEventList().get(0).getEventTypeBCSM(),EventTypeBCSM.routeSelectFailure);
		assertEquals(elem.getBCSMEventList().get(0).getMonitorMode(),MonitorMode.interrupted);
		
		assertEquals(elem.getBCSMEventList().get(1).getEventTypeBCSM(),EventTypeBCSM.oCalledPartyBusy);
		assertEquals(elem.getBCSMEventList().get(1).getMonitorMode(),MonitorMode.interrupted);
		
		assertEquals(elem.getBCSMEventList().get(2).getEventTypeBCSM(),EventTypeBCSM.oAnswer);
		assertEquals(elem.getBCSMEventList().get(2).getMonitorMode(),MonitorMode.interrupted);
		
		assertEquals(elem.getBCSMEventList().get(3).getEventTypeBCSM(),EventTypeBCSM.oCalledPartyNotReachable);
		assertEquals(elem.getBCSMEventList().get(3).getMonitorMode(),MonitorMode.interrupted);
		
		assertEquals(elem.getBCSMEventList().get(4).getEventTypeBCSM(),EventTypeBCSM.oDisconnect);
		assertEquals(elem.getBCSMEventList().get(4).getMonitorMode(),MonitorMode.interrupted);
		assertNotNull(elem.getBCSMEventList().get(4).getLegID());
		assertNotNull(elem.getBCSMEventList().get(4).getLegID().getSendingSideID());
		assertEquals(elem.getBCSMEventList().get(4).getLegID().getSendingSideID(),LegType.leg2);
		
		assertEquals(elem.getBCSMEventList().get(5).getEventTypeBCSM(),EventTypeBCSM.oNoAnswer);
		assertEquals(elem.getBCSMEventList().get(5).getMonitorMode(),MonitorMode.interrupted);
		assertNotNull(elem.getBCSMEventList().get(5).getDpSpecificCriteria());
		assertNotNull(elem.getBCSMEventList().get(5).getDpSpecificCriteria().getApplicationTimer());
		assertEquals(elem.getBCSMEventList().get(5).getDpSpecificCriteria().getApplicationTimer(),new Integer(34));
		
		logger.info(elem);
	}
	
	@Test(groups = { "functional.encode", "circuitSwitchedCall" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(RequestReportBCSMEventRequestImpl.class);
	    	
		List<BCSMEvent> bcsmEventList=new ArrayList<BCSMEvent>();
		bcsmEventList.add(new BCSMEventImpl(EventTypeBCSM.routeSelectFailure, MonitorMode.interrupted, null, null, false));
		bcsmEventList.add(new BCSMEventImpl(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, null, null, false));
		bcsmEventList.add(new BCSMEventImpl(EventTypeBCSM.oAnswer, MonitorMode.interrupted, null, null, false));
		bcsmEventList.add(new BCSMEventImpl(EventTypeBCSM.oCalledPartyNotReachable, MonitorMode.interrupted, null, null, false));
		bcsmEventList.add(new BCSMEventImpl(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, new LegIDImpl(null, LegType.leg2), null, false));
		bcsmEventList.add(new BCSMEventImpl(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, null, new DpSpecificCriteriaImpl(34), false));
		RequestReportBCSMEventRequestImpl elem = new RequestReportBCSMEventRequestImpl(bcsmEventList, null, null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}
