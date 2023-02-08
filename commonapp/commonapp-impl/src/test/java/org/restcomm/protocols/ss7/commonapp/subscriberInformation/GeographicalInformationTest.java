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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TypeOfShape;
import org.testng.annotations.Test;

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
public class GeographicalInformationTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 8, 16, 30, -109, -23, 121, -103, -103, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 8, 16, -28, 6, 95, -128, 91, 6, 20 }; // 5
    }

    private byte[] getEncodedData01() {
        return new byte[] { 4, 8, 16, (byte) 0xAC, 0x16, (byte) 0xC1, (byte) 0xA5, (byte) 0xB0, 0x5B, 0 };
    }

    private byte[] getEncodedData02() {
        return new byte[] { 4, 8, 16, (byte) 0x2C, 0x16, (byte) 0xC1, (byte) 0x25, (byte) 0xB0, 0x5B, 0 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GeographicalInformationImpl.class);
    	
        byte[] rawData = getEncodedData01();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GeographicalInformationImpl);
        GeographicalInformationImpl impl = (GeographicalInformationImpl)result.getResult();
        
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - (-31)) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - (-127.00001)) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);

        rawData = getEncodedData02();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GeographicalInformationImpl);
        impl = (GeographicalInformationImpl)result.getResult();

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 31) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - 53) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);

        rawData = getEncodedData();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GeographicalInformationImpl);
        impl = (GeographicalInformationImpl)result.getResult();

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 21.5) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - 171) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GeographicalInformationImpl);
        impl = (GeographicalInformationImpl)result.getResult();

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - (-70.33)) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - (-179.5)) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 57.27) < 0.01);
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GeographicalInformationImpl.class);
    	
        GeographicalInformationImpl impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                -31, -127.00001, 0);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData01();
        assertEquals(rawData, encodedData);

        impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                31, 53, 0);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData02();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                21.5, 171, 0);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, -70.33, -179.5, 58);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}