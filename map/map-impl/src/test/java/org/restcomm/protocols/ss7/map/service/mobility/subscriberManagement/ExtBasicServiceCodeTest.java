/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtBasicServiceCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 3, (byte) 130, 1, 22 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 3, (byte) 131, 1, 17 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 3, (byte) 131, 1, 16 };
    }

    private byte[] getData1() {
        return new byte[] { 22 };
    }

    private byte[] getData2() {
        return new byte[] { 17 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtBasicServiceCodeImpl.class);
    	        
        byte[] rawData = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtBasicServiceCodeImpl);
        ExtBasicServiceCodeImpl impl = (ExtBasicServiceCodeImpl)result.getResult();
        
        assertTrue(Arrays.equals(impl.getExtBearerService().getData(), this.getData1()));
        assertNull(impl.getExtTeleservice());

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtBasicServiceCodeImpl);
        impl = (ExtBasicServiceCodeImpl)result.getResult();
        assertTrue(Arrays.equals(impl.getExtTeleservice().getData(), this.getData2()));
        assertNull(impl.getExtBearerService());

        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtBasicServiceCodeImpl);
        impl = (ExtBasicServiceCodeImpl)result.getResult();
        assertEquals(impl.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);
        assertNull(impl.getExtBearerService());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtBasicServiceCodeImpl.class);
    	        
    	ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(this.getData1());
    	ExtBasicServiceCodeImpl impl = new ExtBasicServiceCodeImpl(b);
    	ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
    	byte[] rawData = getEncodedData1();
    	assertTrue(Arrays.equals(rawData, encodedData));
        
    	ExtTeleserviceCodeImpl t = new ExtTeleserviceCodeImpl(this.getData2());
    	impl = new ExtBasicServiceCodeImpl(t);
    	buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
    	rawData = getEncodedData2();
    	assertTrue(Arrays.equals(rawData, encodedData));
        
    	t = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
    	impl = new ExtBasicServiceCodeImpl(t);
    	buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
    	rawData = getEncodedData3();
    	assertTrue(Arrays.equals(rawData, encodedData));        
    }
}