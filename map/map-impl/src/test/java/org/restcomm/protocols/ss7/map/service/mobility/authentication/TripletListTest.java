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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
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
public class TripletListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 72, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0, 48, 34, 4, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 4, 4, 4, 4, 4, 4, 4, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
    }

    public static byte[] getRandData() {
        return new byte[] { 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16 };
    }

    public static byte[] getSresData() {
        return new byte[] { 4, 4, 4, 4 };
    }

    public static byte[] getKcData() {
        return new byte[] { 8, 8, 8, 8, 8, 8, 8, 8 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TripletListImpl.class);

        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TripletListImpl);
        TripletListImpl asc = (TripletListImpl)result.getResult();
        
        assertEquals(asc.getAuthenticationTriplets().size(), 1);

        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getRand(), AuthenticationTripletTest.getRandData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getSres(), AuthenticationTripletTest.getSresData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getKc(), AuthenticationTripletTest.getKcData()));

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TripletListImpl);
        asc = (TripletListImpl)result.getResult();

        assertEquals(asc.getAuthenticationTriplets().size(), 2);

        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getRand(), AuthenticationTripletTest.getRandData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getSres(), AuthenticationTripletTest.getSresData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getKc(), AuthenticationTripletTest.getKcData()));

        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(1).getRand(), getRandData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(1).getSres(), getSresData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(1).getKc(), getKcData()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TripletListImpl.class);

        AuthenticationTripletImpl d1 = new AuthenticationTripletImpl(AuthenticationTripletTest.getRandData(),
                AuthenticationTripletTest.getSresData(), AuthenticationTripletTest.getKcData());
        List<AuthenticationTriplet> arr = new ArrayList<AuthenticationTriplet>();
        arr.add(d1);
        TripletListImpl asc = new TripletListImpl(arr);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        d1 = new AuthenticationTripletImpl(AuthenticationTripletTest.getRandData(), AuthenticationTripletTest.getSresData(),
                AuthenticationTripletTest.getKcData());
        AuthenticationTripletImpl d2 = new AuthenticationTripletImpl(getRandData(), getSresData(), getKcData());
        arr = new ArrayList<AuthenticationTriplet>();
        arr.add(d1);
        arr.add(d2);
        asc = new TripletListImpl(arr);

        data=getEncodedData2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}