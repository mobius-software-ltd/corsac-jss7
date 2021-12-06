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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

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
public class AuthenticationTripletTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 34, 4, 16, 15, (byte) 254, 18, (byte) 164, (byte) 207, 43, (byte) 221, (byte) 185, (byte) 178,
                (byte) 158, 109, 83, (byte) 180, (byte) 169, 77, (byte) 128, 4, 4, (byte) 224, 82, (byte) 239, (byte) 242, 4,
                8, 31, 72, (byte) 163, 97, 78, (byte) 239, (byte) 204, 0 };
    }

    static protected byte[] getRandData() {
        return new byte[] { 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128 };
    }

    static protected byte[] getSresData() {
        return new byte[] { -32, 82, -17, -14 };
    }

    static protected byte[] getKcData() {
        return new byte[] { 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationTripletImpl.class);

    	byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AuthenticationTripletImpl);
        AuthenticationTripletImpl asc = (AuthenticationTripletImpl)result.getResult();
        
        assertTrue(Arrays.equals(asc.getRand(), getRandData()));
        assertTrue(Arrays.equals(asc.getSres(), getSresData()));
        assertTrue(Arrays.equals(asc.getKc(), getKcData()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationTripletImpl.class);

        AuthenticationTripletImpl asc = new AuthenticationTripletImpl(getRandData(), getSresData(), getKcData());
        byte[] data=getEncodedData();        		
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
