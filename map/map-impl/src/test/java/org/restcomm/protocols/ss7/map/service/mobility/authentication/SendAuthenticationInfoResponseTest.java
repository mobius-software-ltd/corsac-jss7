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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTripletImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAvImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletListImpl;
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
public class SendAuthenticationInfoResponseTest {

    private byte[] getEncodedData_V3_tripl() {
        return new byte[] { (byte) 163, 38, -96, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76,
                -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    private byte[] getEncodedData_V3_Eps() {
        return new byte[] { (byte) 163, 80, -94, 78, 48, 76, 4, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 2, 2,
                2, 2, 4, 16, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 32, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 };
    }

    private byte[] getEncodedData_V2_tripl() {
        return new byte[] { 48, 36, 48, 34, 4, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 4, 4, 4, 4,
                4, 4, 4, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendAuthenticationInfoResponseImplV3.class);
    	parser.replaceClass(SendAuthenticationInfoResponseImplV1.class);
    
        byte[] rawData = getEncodedData_V3_tripl();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendAuthenticationInfoResponse);
        SendAuthenticationInfoResponse asc = (SendAuthenticationInfoResponse)result.getResult();
        
        AuthenticationSetListImpl asl = asc.getAuthenticationSetList();
        assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
        assertNull(asl.getQuintupletList());

        assertNull(asc.getEpsAuthenticationSetList());
        assertNull(asc.getExtensionContainer());

        rawData = getEncodedData_V3_Eps();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendAuthenticationInfoResponse);
        asc = (SendAuthenticationInfoResponse)result.getResult();
        
        asl = asc.getAuthenticationSetList();
        assertNull(asl);

        EpsAuthenticationSetListImpl easl = asc.getEpsAuthenticationSetList();
        assertEquals(easl.getEpcAv().size(), 1);
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getRand(), EpcAvTest.getRandData()));
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getXres(), EpcAvTest.getXresData()));
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getAutn(), EpcAvTest.getAutnData()));
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getKasme(), EpcAvTest.getKasmeData()));

        assertNull(asc.getExtensionContainer());

        rawData = getEncodedData_V2_tripl();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendAuthenticationInfoResponse);
        asc = (SendAuthenticationInfoResponse)result.getResult();
        
        asl = asc.getAuthenticationSetList();
        assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
        assertTrue(Arrays.equals(asl.getTripletList().getAuthenticationTriplets().get(0).getRand(),
                TripletListTest.getRandData()));
        assertNull(asl.getQuintupletList());

        assertNull(asc.getEpsAuthenticationSetList());
        assertNull(asc.getExtensionContainer());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendAuthenticationInfoResponseImplV3.class);
    	parser.replaceClass(SendAuthenticationInfoResponseImplV1.class);
    
        ArrayList<AuthenticationTripletImpl> ats = new ArrayList<AuthenticationTripletImpl>();
        AuthenticationTripletImpl at = new AuthenticationTripletImpl(AuthenticationTripletTest.getRandData(), AuthenticationTripletTest.getSresData(), AuthenticationTripletTest.getKcData());
        ats.add(at);
        TripletListImpl tl = new TripletListImpl(ats);
        AuthenticationSetListImpl asl = new AuthenticationSetListImpl(tl,3);
        SendAuthenticationInfoResponse asc = new SendAuthenticationInfoResponseImplV3(3, asl, null, null);
        
        byte[] data=getEncodedData_V3_tripl();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        EpcAvImpl d1 = new EpcAvImpl(EpcAvTest.getRandData(), EpcAvTest.getXresData(), EpcAvTest.getAutnData(), EpcAvTest.getKasmeData(), null);
        ArrayList<EpcAvImpl> epcAvs = new ArrayList<EpcAvImpl>();
        epcAvs.add(d1);
        EpsAuthenticationSetListImpl easl = new EpsAuthenticationSetListImpl(epcAvs);
        asc = new SendAuthenticationInfoResponseImplV3(3, null, null, easl);

        data=getEncodedData_V3_Eps();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        ats = new ArrayList<AuthenticationTripletImpl>();
        at = new AuthenticationTripletImpl(TripletListTest.getRandData(), TripletListTest.getSresData(),
                TripletListTest.getKcData());
        ats.add(at);
        tl = new TripletListImpl(ats);
        asl = new AuthenticationSetListImpl(tl,2);        
        asc = new SendAuthenticationInfoResponseImplV1(2, asl);

        data=getEncodedData_V2_tripl();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));        
    }
}