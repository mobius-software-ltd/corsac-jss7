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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author kostiantyn nosach
* @author yulianoifa
*
*/

public class CorrelationIDTest {

    private byte[] getEncodedData() {
        return new byte[] {48, 7, -128, 5, 17, 17, 33, 34, 34};
    }

    private byte[] getEncodedDataFull() {
        return new byte[] {48, 61, -128, 5, 17, 17, 33, 34, 34, -127, 27, 115, 105, 112, 58, 107, 111, 110, 
                115, 116, 97, 110, 116, 105, 110, 64, 116, 101, 108, 101, 115, 116, 97, 120, 46, 99, 111, 109, 
                -126, 23, 115, 105, 112, 58, 110, 111, 115, 97, 99, 104, 64, 116, 101, 108, 101, 115, 116, 97, 120, 46, 99, 111, 109};
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CorrelationIDImpl.class);
    	        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CorrelationIDImpl);
        CorrelationIDImpl correlationId = (CorrelationIDImpl)result.getResult();
        
        assertTrue(correlationId.getHlrId().getData().equals("1111122222"));

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CorrelationIDImpl);
        correlationId = (CorrelationIDImpl)result.getResult();

        assertTrue(correlationId.getHlrId().getData().equals("1111122222"));
        assertEquals(correlationId.getSipUriA().getValue().toString(Charset.forName("UTF-8")), "sip:konstantin@telestax.com");
        assertEquals(correlationId.getSipUriB().getValue().toString(Charset.forName("UTF-8")), "sip:nosach@telestax.com");
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CorrelationIDImpl.class);

        IMSIImpl hlrId = new IMSIImpl("1111122222");

        CorrelationIDImpl cid = new CorrelationIDImpl(hlrId, null, null);
        ByteBuf buffer=parser.encode(cid);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        byte [] dataSipA = "sip:konstantin@telestax.com".getBytes();
        byte [] dataSipB = "sip:nosach@telestax.com".getBytes();

        SipUriImpl sipA = new SipUriImpl(Unpooled.wrappedBuffer(dataSipA));
        SipUriImpl sipB = new SipUriImpl(Unpooled.wrappedBuffer(dataSipB));
        
        cid = new CorrelationIDImpl(hlrId, sipA, sipB);
        buffer=parser.encode(cid);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }
}

