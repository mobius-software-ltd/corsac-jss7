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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ConnectRequestImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
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
public class ConnectTest 
{
	protected final transient Logger logger=LogManager.getLogger(ConnectTest.class);

	private byte[] message1=new byte[] { 0x30,0x0c,(byte)0xa0,0x0a,0x04,0x08,(byte)0x84,(byte)0x90,
			(byte)0x81,0x67,0x48,0x00,0x14,0x09 };
	
	private byte[] calledPartyData1=new byte[] { -124, -112, -127, 103, 72, 0, 20, 9 };
	 
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ConnectRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof ConnectRequestImpl);
	        
		ConnectRequestImpl elem = (ConnectRequestImpl)result.getResult();
		
		assertNotNull(elem.getDestinationRoutingAddress());
		assertNotNull(elem.getDestinationRoutingAddress().getCalledPartyNumber());
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
		assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(calledPartyData1),CalledPartyNumberIsupImpl.translate(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber())));
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNumberingPlanIndicator(), 1);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getInternalNetworkNumberIndicator(), 1);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNatureOfAddressIndicator(), 4);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress(), "18768400419");
		
		logger.info(elem);		
	}
	
	@Test
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(ConnectRequestImpl.class);
	    	
		List<CalledPartyNumberIsup> calledPartyNumber=new ArrayList<CalledPartyNumberIsup>();
		calledPartyNumber.add(new CalledPartyNumberIsupImpl(new CalledPartyNumberImpl(Unpooled.wrappedBuffer(calledPartyData1))));
		DestinationRoutingAddress destinationAddress=new DestinationRoutingAddressImpl(calledPartyNumber);
		
		ConnectRequestImpl elem = new ConnectRequestImpl(destinationAddress, null, null, null, null, null,
				null, null, null, null, null, null, null,null, null, null, null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}