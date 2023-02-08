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
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
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

        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(0).getRand(),Unpooled.wrappedBuffer(AuthenticationTripletTest.getRandData())));
        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(0).getSres(),Unpooled.wrappedBuffer(AuthenticationTripletTest.getSresData())));
        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(0).getKc(), Unpooled.wrappedBuffer(AuthenticationTripletTest.getKcData())));

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TripletListImpl);
        asc = (TripletListImpl)result.getResult();

        assertEquals(asc.getAuthenticationTriplets().size(), 2);

        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(0).getRand(),Unpooled.wrappedBuffer(AuthenticationTripletTest.getRandData())));
        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(0).getSres(),Unpooled.wrappedBuffer(AuthenticationTripletTest.getSresData())));
        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(0).getKc(),Unpooled.wrappedBuffer(AuthenticationTripletTest.getKcData())));

        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(1).getRand(),Unpooled.wrappedBuffer(getRandData())));
        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(1).getSres(),Unpooled.wrappedBuffer(getSresData())));
        assertTrue(ByteBufUtil.equals(asc.getAuthenticationTriplets().get(1).getKc(),Unpooled.wrappedBuffer(getKcData())));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TripletListImpl.class);

        AuthenticationTripletImpl d1 = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(AuthenticationTripletTest.getRandData()),
        		Unpooled.wrappedBuffer(AuthenticationTripletTest.getSresData()), Unpooled.wrappedBuffer(AuthenticationTripletTest.getKcData()));
        List<AuthenticationTriplet> arr = new ArrayList<AuthenticationTriplet>();
        arr.add(d1);
        TripletListImpl asc = new TripletListImpl(arr);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        d1 = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(AuthenticationTripletTest.getRandData()), Unpooled.wrappedBuffer(AuthenticationTripletTest.getSresData()),
        		Unpooled.wrappedBuffer(AuthenticationTripletTest.getKcData()));
        AuthenticationTripletImpl d2 = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(getRandData()), Unpooled.wrappedBuffer(getSresData()), Unpooled.wrappedBuffer(getKcData()));
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