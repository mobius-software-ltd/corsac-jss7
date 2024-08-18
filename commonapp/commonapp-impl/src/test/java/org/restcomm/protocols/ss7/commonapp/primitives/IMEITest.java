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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class IMEITest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 8, 33, 67, 101, (byte) 135, 9, 33, 67, 101 };
    }

    private byte[] getEncodedDataImeiLengthLessThan15() {
        return new byte[] { 4, 1, -15 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(IMEIImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IMEIImpl);
        IMEIImpl imei = (IMEIImpl)result.getResult();
        
        assertEquals(imei.getIMEI(), "1234567890123456");

        // Testing IMEI length != 15
        rawData = getEncodedDataImeiLengthLessThan15();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IMEIImpl);
        imei = (IMEIImpl)result.getResult();

        assertEquals(imei.getIMEI(), "1");
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(IMEIImpl.class);
    	
        IMEIImpl imei = new IMEIImpl("1234567890123456");
        ByteBuf buffer=parser.encode(imei);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing IMEI length != 15
        imei = new IMEIImpl("1");
        buffer=parser.encode(imei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataImeiLengthLessThan15();

        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
