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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class RequestedInfoTest {

    // Real Trace
    byte[] data = new byte[] { 48, 0x04, (byte) 0x80, 0x00, (byte) 0x81, 0x00 };
    byte[] dataFull = new byte[] { 48, 56, -128, 0, -127, 0, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
            5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 1, 1, -122,
            0, -123, 0, -121, 0 };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RequestedInfoImpl.class);
    	                
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestedInfoImpl);
        RequestedInfoImpl requestedInfo = (RequestedInfoImpl)result.getResult();
        
        assertTrue(requestedInfo.getLocationInformation());
        assertTrue(requestedInfo.getSubscriberState());
        assertNull(requestedInfo.getExtensionContainer());
        assertFalse(requestedInfo.getCurrentLocation());
        assertNull(requestedInfo.getRequestedDomain());
        assertFalse(requestedInfo.getImei());
        assertFalse(requestedInfo.getMsClassmark());
        assertFalse(requestedInfo.getMnpRequestedInfo());

        result=parser.decode(Unpooled.wrappedBuffer(dataFull));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestedInfoImpl);
        requestedInfo = (RequestedInfoImpl)result.getResult();

        assertTrue(requestedInfo.getLocationInformation());
        assertTrue(requestedInfo.getSubscriberState());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(requestedInfo.getExtensionContainer()));
        assertTrue(requestedInfo.getCurrentLocation());
        assertEquals(requestedInfo.getRequestedDomain(), DomainType.psDomain);
        assertTrue(requestedInfo.getImei());
        assertTrue(requestedInfo.getMsClassmark());
        assertTrue(requestedInfo.getMnpRequestedInfo());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RequestedInfoImpl.class);
    	                
        RequestedInfoImpl requestedInfo = new RequestedInfoImpl(true, true, null, false, null, false, false, false);
        ByteBuf buffer=parser.encode(requestedInfo);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));

        requestedInfo = new RequestedInfoImpl(true, true, MAPExtensionContainerTest.GetTestExtensionContainer(), true,
                DomainType.psDomain, true, true, true);
        buffer=parser.encode(requestedInfo);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, dataFull));
    }
}
