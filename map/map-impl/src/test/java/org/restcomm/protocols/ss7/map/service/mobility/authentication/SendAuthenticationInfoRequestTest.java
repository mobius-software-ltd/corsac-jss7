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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
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
public class SendAuthenticationInfoRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 21, -128, 6, 17, 33, 34, 51, 67, 68, 2, 1, 4, -125, 1, 0, -124, 3, 0x04, 0x15, (byte) 0x93, -122, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 57, -128, 6, 51, 51, 67, 68, 68, -12, 2, 1, 5, 5, 0, -127, 0, 48, 34, 4, 16, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 14, 2, 2, 2, 2, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, -125, 1, 1, -123, 1, 6 };
    }

    private byte[] getEncodedData_V2() {
        return new byte[] { 4, 8, 82, 0, 7, 34, 2, 35, 103, -9 };
    }
    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendAuthenticationInfoRequestImplV3.class);
    	parser.replaceClass(SendAuthenticationInfoRequestImplV1.class);

        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendAuthenticationInfoRequest);
        SendAuthenticationInfoRequest asc = (SendAuthenticationInfoRequest)result.getResult();
        
        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));
        assertEquals(asc.getRequestingNodeType(), RequestingNodeType.vlr);
        assertEquals(asc.getNumberOfRequestedVectors(), 4);

        assertNotNull(asc.getRequestingPlmnId());
        assertEquals(asc.getRequestingPlmnId().getMcc(), 405);
        assertEquals(asc.getRequestingPlmnId().getMnc(), 391);

        assertNull(asc.getReSynchronisationInfo());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getNumberOfRequestedAdditionalVectors());

        assertFalse(asc.getSegmentationProhibited());
        assertFalse(asc.getImmediateResponsePreferred());
        assertTrue(asc.getAdditionalVectorsAreForEPS());

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendAuthenticationInfoRequest);
        asc = (SendAuthenticationInfoRequest)result.getResult();

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("33333444444"));
        assertEquals(asc.getRequestingNodeType(), RequestingNodeType.sgsn);
        assertEquals(asc.getNumberOfRequestedVectors(), 5);

        assertNull(asc.getRequestingPlmnId());

        ReSynchronisationInfo rsi = asc.getReSynchronisationInfo();
        assertTrue(ByteBufUtil.equals(rsi.getRand(), Unpooled.wrappedBuffer(ReSynchronisationInfoTest.getRandData())));
        assertTrue(ByteBufUtil.equals(rsi.getAuts(), Unpooled.wrappedBuffer(ReSynchronisationInfoTest.getAutsData())));

        assertNull(asc.getExtensionContainer());
        assertEquals((int) asc.getNumberOfRequestedAdditionalVectors(), 6);

        assertTrue(asc.getSegmentationProhibited());
        assertTrue(asc.getImmediateResponsePreferred());
        assertFalse(asc.getAdditionalVectorsAreForEPS());

        rawData = getEncodedData_V2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendAuthenticationInfoRequest);
        asc = (SendAuthenticationInfoRequest)result.getResult();

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("250070222032767"));
        assertNull(asc.getRequestingNodeType());
        assertEquals(asc.getNumberOfRequestedVectors(), 0);

        assertNull(asc.getRequestingPlmnId());

        assertNull(asc.getReSynchronisationInfo());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getNumberOfRequestedAdditionalVectors());

        assertFalse(asc.getSegmentationProhibited());
        assertFalse(asc.getImmediateResponsePreferred());
        assertFalse(asc.getAdditionalVectorsAreForEPS());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendAuthenticationInfoRequestImplV3.class);
    	parser.replaceClass(SendAuthenticationInfoRequestImplV1.class);

        IMSIImpl imsi = new IMSIImpl("111222333444");
        PlmnIdImpl plmnId = new PlmnIdImpl(405,391);
        SendAuthenticationInfoRequest asc = new SendAuthenticationInfoRequestImplV3(imsi, 4, false, false, null, null, RequestingNodeType.vlr, plmnId, null, true);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        imsi = new IMSIImpl("33333444444");
        ReSynchronisationInfoImpl rsi = new ReSynchronisationInfoImpl(Unpooled.wrappedBuffer(ReSynchronisationInfoTest.getRandData()), Unpooled.wrappedBuffer(ReSynchronisationInfoTest.getAutsData()));
        asc = new SendAuthenticationInfoRequestImplV3(imsi, 5, true, true, rsi, null, RequestingNodeType.sgsn, null, 6, false);

        data=getEncodedData2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        imsi = new IMSIImpl("250070222032767");
        asc = new SendAuthenticationInfoRequestImplV1(imsi);

        data=getEncodedData_V2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}