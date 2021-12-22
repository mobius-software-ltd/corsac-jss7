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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.TestOctetStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OctetStringBaseTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 5, 1, 2, 3, 4, 5 };
    }

    private byte[] getEncodedDataTooShort() {
        return new byte[] { 4, 2, 1 };
    }

    private byte[] getEncodedDataTooLong() {
        return new byte[] { 4, 6, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    private byte[] getData() {
        return new byte[] { 1, 2, 3, 4, 5 };
    }

    private byte[] getDataTooShort() {
        return new byte[] { 1 };
    }

    private byte[] getDataTooLong() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TestOctetStringImpl.class);
    	
        // correct data
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TestOctetStringImpl);
        TestOctetStringImpl pi = (TestOctetStringImpl)result.getResult();        
        
        assertTrue(Arrays.equals(getData(), pi.getData()));

        // bad data
        rawData = getEncodedDataTooShort();
        try {
        	parser.decode(Unpooled.wrappedBuffer(rawData));
        } catch (ASNException e) {
            assertNotNull(e);
        }        
        
        rawData = getEncodedDataTooLong();
        try {
        	parser.decode(Unpooled.wrappedBuffer(rawData));
        } catch (ASNException e) {
            assertNotNull(e);
        }        
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TestOctetStringImpl.class);
    	
        // correct data
        TestOctetStringImpl pi = new TestOctetStringImpl(getData());
        ByteBuf buffer = parser.encode(pi);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // bad data
        pi = new TestOctetStringImpl(null);
        try {
        	parser.encode(pi);                       
        } catch (ASNException e) {
            assertNotNull(e);
        }

        pi = new TestOctetStringImpl(getDataTooShort());
        try {
        	parser.encode(pi);                       
        } catch (ASNException e) {
            assertNotNull(e);
        }

        pi = new TestOctetStringImpl(getDataTooLong());
        try {
        	parser.encode(pi);                       
        } catch (ASNException e) {
            assertNotNull(e);
        }
    }

    @Test(groups = { "functional.encode", "equality" })
    public void testEqality() throws Exception {

        byte[] testD1 = new byte[2];
        byte[] testD2 = new byte[2];
        byte[] testD3 = new byte[2];
        testD1[0] = 11;
        testD1[1] = 12;
        testD2[0] = 11;
        testD2[1] = 12;
        testD3[0] = 21;
        testD3[1] = 22;

        ExtPDPTypeImpl imp1 = new ExtPDPTypeImpl(testD1);
        ExtPDPTypeImpl imp2 = new ExtPDPTypeImpl(testD2);
        ExtPDPTypeImpl imp3 = new ExtPDPTypeImpl(testD3);
        
        assertTrue(Arrays.equals(imp1.getData(),imp1.getData()));
        assertTrue(Arrays.equals(imp1.getData(),imp2.getData()));
        assertFalse(Arrays.equals(imp1.getData(),imp3.getData()));
        assertFalse(Arrays.equals(imp2.getData(),imp3.getData()));
    }
}