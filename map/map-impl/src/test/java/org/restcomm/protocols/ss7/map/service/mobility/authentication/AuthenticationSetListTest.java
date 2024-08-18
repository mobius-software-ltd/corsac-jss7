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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletList;
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
public class AuthenticationSetListTest {

    private byte[] getEncodedData_V3_tripl() {
        return new byte[] { -80, 38, -96, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    private byte[] getEncodedData_V3_q() {
        return new byte[] { -80, 82, -95, 80, 48, 78, 4, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 2, 2, 2, 2, 4, 16, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 16, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 16, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
    }

    private byte[] getEncodedData_V2_tripl() {
        return new byte[] { 48, 38, 48, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationSetListV1Impl.class);
    	parser.replaceClass(AuthenticationSetListV3Impl.class);

    	byte[] rawData = getEncodedData_V3_tripl();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AuthenticationSetListV3Impl);
        AuthenticationSetList asc = (AuthenticationSetListV3Impl)result.getResult();
        
        assertEquals(asc.getTripletList().getAuthenticationTriplets().size(), 1);
        assertNull(asc.getQuintupletList());

        rawData = getEncodedData_V2_tripl();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AuthenticationSetListV1Impl);
        asc = (AuthenticationSetListV1Impl)result.getResult();
        
        assertEquals(asc.getTripletList().getAuthenticationTriplets().size(), 1);
        assertNull(asc.getQuintupletList());

        rawData = getEncodedData_V3_q();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AuthenticationSetListV3Impl);
        asc = (AuthenticationSetListV3Impl)result.getResult();

        assertNull(asc.getTripletList());
        assertEquals(asc.getQuintupletList().getAuthenticationQuintuplets().size(), 1);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationSetListV1Impl.class);
    	parser.replaceClass(AuthenticationSetListV3Impl.class);

        List<AuthenticationTriplet> ats = new ArrayList<AuthenticationTriplet>();
        AuthenticationTripletImpl at = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(AuthenticationTripletTest.getRandData()),
        		Unpooled.wrappedBuffer(AuthenticationTripletTest.getSresData()), Unpooled.wrappedBuffer(AuthenticationTripletTest.getKcData()));
        ats.add(at);
        TripletList tl = new TripletListImpl(ats);
        AuthenticationSetList asc = new AuthenticationSetListV3Impl(tl);        

        byte[] data=getEncodedData_V3_tripl();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        ats = new ArrayList<AuthenticationTriplet>();
        at = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(AuthenticationTripletTest.getRandData()), Unpooled.wrappedBuffer(AuthenticationTripletTest.getSresData()),
        		Unpooled.wrappedBuffer(AuthenticationTripletTest.getKcData()));
        ats.add(at);
        tl = new TripletListImpl(ats);
        asc = new AuthenticationSetListV1Impl(tl);
        
        data=getEncodedData_V2_tripl();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        List<AuthenticationQuintuplet> qts = new ArrayList<AuthenticationQuintuplet>();
        AuthenticationQuintupletImpl qt = new AuthenticationQuintupletImpl(Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getRandData()),
        		Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getXresData()), Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getCkData()),
        		Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getIkData()), Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getAutnData()));
        qts.add(qt);
        QuintupletList ql = new QuintupletListImpl(qts);
        asc = new AuthenticationSetListV3Impl(ql);

        data=getEncodedData_V3_q();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}