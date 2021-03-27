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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
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
public class SendAuthenticationInfoRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 21, -128, 6, 17, 33, 34, 51, 67, 68, 2, 1, 4, -125, 1, 0, -124, 3, -71, -2, -59, -122, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 57, -128, 6, 51, 51, 67, 68, 68, -12, 2, 1, 5, 5, 0, -127, 0, 48, 34, 4, 16, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 14, 2, 2, 2, 2, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, -125, 1, 1, -123, 1, 6 };
    }

    private byte[] getEncodedData_V2() {
        return new byte[] { 4, 8, 82, 0, 7, 34, 2, 35, 103, -9 };
    }

    private byte[] getRequestingPlmnId() {
        return new byte[] { (byte) 185, (byte) 254, (byte) 197 };
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
        
        IMSIImpl imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));
        assertEquals(asc.getRequestingNodeType(), RequestingNodeType.vlr);
        assertEquals(asc.getNumberOfRequestedVectors(), 4);

        assertNotNull(asc.getRequestingPlmnId());
        assertTrue(Arrays.equals(asc.getRequestingPlmnId().getData(), getRequestingPlmnId()));

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

        ReSynchronisationInfoImpl rsi = asc.getReSynchronisationInfo();
        assertTrue(Arrays.equals(rsi.getRand(), ReSynchronisationInfoTest.getRandData()));
        assertTrue(Arrays.equals(rsi.getAuts(), ReSynchronisationInfoTest.getAutsData()));

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

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendAuthenticationInfoRequestImplV3.class);
    	parser.replaceClass(SendAuthenticationInfoRequestImplV1.class);

        IMSIImpl imsi = new IMSIImpl("111222333444");
        PlmnIdImpl plmnId = new PlmnIdImpl(getRequestingPlmnId());
        SendAuthenticationInfoRequest asc = new SendAuthenticationInfoRequestImplV3(3, imsi, 4, false, false, null, null, RequestingNodeType.vlr, plmnId, null, true);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        imsi = new IMSIImpl("33333444444");
        ReSynchronisationInfoImpl rsi = new ReSynchronisationInfoImpl(ReSynchronisationInfoTest.getRandData(), ReSynchronisationInfoTest.getAutsData());
        asc = new SendAuthenticationInfoRequestImplV3(3, imsi, 5, true, true, rsi, null, RequestingNodeType.sgsn, null, 6, false);

        data=getEncodedData2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        imsi = new IMSIImpl("250070222032767");
        asc = new SendAuthenticationInfoRequestImplV1(2, imsi);

        data=getEncodedData_V2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}