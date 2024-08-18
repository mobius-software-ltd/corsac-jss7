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

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletList;
import org.junit.Test;

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
        
        AuthenticationSetList asl = asc.getAuthenticationSetList();
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

        EpsAuthenticationSetList easl = asc.getEpsAuthenticationSetList();
        assertEquals(easl.getEpcAv().size(), 1);
        assertTrue(ByteBufUtil.equals(easl.getEpcAv().get(0).getRand(), Unpooled.wrappedBuffer(EpcAvTest.getRandData())));
        assertTrue(ByteBufUtil.equals(easl.getEpcAv().get(0).getXres(), Unpooled.wrappedBuffer(EpcAvTest.getXresData())));
        assertTrue(ByteBufUtil.equals(easl.getEpcAv().get(0).getAutn(), Unpooled.wrappedBuffer(EpcAvTest.getAutnData())));
        assertTrue(ByteBufUtil.equals(easl.getEpcAv().get(0).getKasme(), Unpooled.wrappedBuffer(EpcAvTest.getKasmeData())));

        assertNull(asc.getExtensionContainer());

        rawData = getEncodedData_V2_tripl();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendAuthenticationInfoResponse);
        asc = (SendAuthenticationInfoResponse)result.getResult();
        
        asl = asc.getAuthenticationSetList();
        assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
        assertTrue(ByteBufUtil.equals(asl.getTripletList().getAuthenticationTriplets().get(0).getRand(),
                Unpooled.wrappedBuffer(TripletListTest.getRandData())));
        assertNull(asl.getQuintupletList());

        assertNull(asc.getEpsAuthenticationSetList());
        assertNull(asc.getExtensionContainer());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendAuthenticationInfoResponseImplV3.class);
    	parser.replaceClass(SendAuthenticationInfoResponseImplV1.class);
    
        List<AuthenticationTriplet> ats = new ArrayList<AuthenticationTriplet>();
        AuthenticationTripletImpl at = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(AuthenticationTripletTest.getRandData()), 
        		Unpooled.wrappedBuffer(AuthenticationTripletTest.getSresData()), Unpooled.wrappedBuffer(AuthenticationTripletTest.getKcData()));
        ats.add(at);
        TripletList tl = new TripletListImpl(ats);
        AuthenticationSetList asl = new AuthenticationSetListV3Impl(tl);
        SendAuthenticationInfoResponse asc = new SendAuthenticationInfoResponseImplV3(asl, null, null);
        
        byte[] data=getEncodedData_V3_tripl();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        EpcAvImpl d1 = new EpcAvImpl(Unpooled.wrappedBuffer(EpcAvTest.getRandData()), Unpooled.wrappedBuffer(EpcAvTest.getXresData()), 
        		Unpooled.wrappedBuffer(EpcAvTest.getAutnData()), Unpooled.wrappedBuffer(EpcAvTest.getKasmeData()), null);
        List<EpcAv> epcAvs = new ArrayList<EpcAv>();
        epcAvs.add(d1);
        EpsAuthenticationSetList easl = new EpsAuthenticationSetListImpl(epcAvs);
        asc = new SendAuthenticationInfoResponseImplV3(null, null, easl);

        data=getEncodedData_V3_Eps();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        ats = new ArrayList<AuthenticationTriplet>();
        at = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(TripletListTest.getRandData()), Unpooled.wrappedBuffer(TripletListTest.getSresData()),
        		Unpooled.wrappedBuffer(TripletListTest.getKcData()));
        ats.add(at);
        tl = new TripletListImpl(ats);
        asl = new AuthenticationSetListV1Impl(tl);        
        asc = new SendAuthenticationInfoResponseImplV1(asl);

        data=getEncodedData_V2_tripl();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));        
    }
}