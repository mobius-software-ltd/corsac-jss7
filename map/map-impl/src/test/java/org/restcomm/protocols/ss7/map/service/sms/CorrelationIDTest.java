/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author kostiantyn nosach
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

    @Test(groups = { "functional.decode", "service.sms" })
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
        assertEquals(new String (correlationId.getSipUriA().getData()), "sip:konstantin@telestax.com");
        assertEquals(new String (correlationId.getSipUriB().getData()), "sip:nosach@telestax.com");
    }

    @Test(groups = { "functional.encode", "service.sms" })
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

        SipUriImpl sipA = new SipUriImpl(dataSipA);
        SipUriImpl sipB = new SipUriImpl(dataSipB);
        
        cid = new CorrelationIDImpl(hlrId, sipA, sipB);
        buffer=parser.encode(cid);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }
}

