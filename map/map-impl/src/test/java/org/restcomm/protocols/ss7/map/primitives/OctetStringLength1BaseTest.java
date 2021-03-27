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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
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
public class OctetStringLength1BaseTest {
    private byte[] getEncodedData() {
        return new byte[] { 4, 1, 1 };
    }

    private byte[] getEncodedDataTooLong() {
        return new byte[] { 4, 8, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TestOctetStringLength1Impl.class);
    	
        // correct data
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TestOctetStringLength1Impl);
        TestOctetStringLength1Impl pi = (TestOctetStringLength1Impl)result.getResult();        
        assertEquals(pi.getData(), 1);

        // bad data
        rawData = getEncodedDataTooLong();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertTrue(result.getHadErrors());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TestOctetStringLength1Impl.class);
    	        
        // correct data
        TestOctetStringLength1Impl pi = new TestOctetStringLength1Impl(1);
        ByteBuf buffer = parser.encode(pi);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.encode", "equality" })
    public void testEqality() throws Exception {

        Ext4QoSSubscribedImpl imp1 = new Ext4QoSSubscribedImpl(10);
        Ext4QoSSubscribedImpl imp2 = new Ext4QoSSubscribedImpl(10);
        Ext4QoSSubscribedImpl imp3 = new Ext4QoSSubscribedImpl(12);

        assertEquals(imp1.getData(),imp1.getData());
        assertEquals(imp1.getData(),imp2.getData());
        assertNotEquals(imp1.getData(),imp3.getData());
        assertNotEquals(imp2.getData(),imp3.getData());
    }
}