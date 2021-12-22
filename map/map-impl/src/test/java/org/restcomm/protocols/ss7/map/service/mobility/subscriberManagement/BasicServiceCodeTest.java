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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
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
public class BasicServiceCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 3, -126, 1, 38 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 3, -125, 1, 33 };
    }

    @Test(groups = { "functional.decode", "service.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(BasicServiceCodeImpl.class);
    	
        byte[] rawData = getEncodedData1();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BasicServiceCodeImpl);
        BasicServiceCodeImpl impl = (BasicServiceCodeImpl)result.getResult();
        
        assertEquals(impl.getBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
        assertNull(impl.getTeleservice());

        rawData = getEncodedData2();

        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BasicServiceCodeImpl);
        impl = (BasicServiceCodeImpl)result.getResult();

        assertEquals(impl.getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);
        assertNull(impl.getBearerService());
    }

    @Test(groups = { "functional.encode", "service.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(BasicServiceCodeImpl.class);
    	
        BearerServiceCodeImpl bearerService = new BearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        BasicServiceCodeImpl impl = new BasicServiceCodeImpl(bearerService);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        TeleserviceCodeImpl teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        impl = new BasicServiceCodeImpl(teleservice);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}