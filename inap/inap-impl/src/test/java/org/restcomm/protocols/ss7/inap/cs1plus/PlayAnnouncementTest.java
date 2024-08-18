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

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InbandInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.PlayAnnouncementRequestImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class PlayAnnouncementTest 
{
	protected final transient Logger logger=LogManager.getLogger(PlayAnnouncementTest.class);

	private byte[] message1=new byte[] { 0x30,0x15,(byte)0xe1,0x03,(byte)0x80,0x01,0x01,(byte)0xa0,
			0x08,(byte)0xa0,0x06,(byte)0xa0,0x04,(byte)0x80,0x02,0x38,0x65,(byte)0x81,0x01,(byte)0xff,
			(byte)0x82,0x01,(byte)0xff};
	
	@BeforeClass
	public static void initTests()
	{
		Configurator.initialize(new DefaultConfiguration());
	}
	
	@Test
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(PlayAnnouncementRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof PlayAnnouncementRequestImpl);
	        
		PlayAnnouncementRequestImpl elem = (PlayAnnouncementRequestImpl)result.getResult();
		assertNotNull(elem.getLegID());
		assertEquals(elem.getLegID(),LegType.leg1);	    
		assertNotNull(elem.getInformationToSend());
		assertNotNull(elem.getInformationToSend().getInbandInfo());
		assertNotNull(elem.getInformationToSend().getInbandInfo().getMessageID());
		assertNotNull(elem.getInformationToSend().getInbandInfo().getMessageID().getElementaryMessageID());
		assertEquals(elem.getInformationToSend().getInbandInfo().getMessageID().getElementaryMessageID(),new Integer(14437));	    
		assertTrue(elem.getDisconnectFromIPForbidden());	    
		assertTrue(elem.getRequestAnnouncementCompleteNotification());	    
		logger.info(elem);
	}
	
	@Test
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
		parser.replaceClass(PlayAnnouncementRequestImpl.class);
	    	
		InformationToSend informationToSend=new InformationToSendImpl(new InbandInfoImpl(new MessageIDImpl(14437), null, null, null));
		PlayAnnouncementRequestImpl elem = new PlayAnnouncementRequestImpl(LegType.leg1,null,informationToSend,true,true,null);
	    byte[] rawData = this.message1;
	    ByteBuf buffer=parser.encode(elem);
	    byte[] encodedData = new byte[buffer.readableBytes()];
	    buffer.readBytes(encodedData);
	    assertTrue(Arrays.equals(rawData, encodedData));
	}
}