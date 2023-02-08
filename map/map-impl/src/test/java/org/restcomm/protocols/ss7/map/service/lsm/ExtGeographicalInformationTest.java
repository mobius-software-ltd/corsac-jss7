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

package org.restcomm.protocols.ss7.map.service.lsm;

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
public class ExtGeographicalInformationTest {

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyCircle() {
        return new byte[] { 4, 8, 16, 92, 113, -57, -106, 11, 97, 7 };
    }

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyEllipse() {
        return new byte[] { 4, 11, 48, -36, 113, -57, 22, 11, 96, 25, 48, 11, 23 };
    }

    private byte[] getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid() {
        return new byte[] { 4, 14, (byte) 144, -35, 39, -46, 22, 65, -3, -128, 17, 18, 41, 14, 14, 29 };
    }

    private byte[] getEncodedData_EllipsoidArc() {
        return new byte[] { 4, 13, (byte) 160, 1, 108, 22, 121, -48, 54, 23, 112, 9, 11, 12, 39 };
    }

    private byte[] getEncodedData_EllipsoidPoint() {
        return new byte[] { 4, 7, 0, 0, 0, 0, -3, -35, -34 };
    }

    @Test(groups = { "functional.decode", "lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtGeographicalInformationImpl.class);
    	
        byte[] data = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtGeographicalInformationImpl);
        ExtGeographicalInformationImpl impl = (ExtGeographicalInformationImpl)result.getResult();
        
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-149)) < 0.01);  // -31
        assertTrue(Math.abs(impl.getUncertainty() - 9.48) < 0.01);

        data = getEncodedData_EllipsoidPointWithUncertaintyEllipse();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtGeographicalInformationImpl);
        impl = (ExtGeographicalInformationImpl)result.getResult();

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyEllipse);
        assertTrue(Math.abs(impl.getLatitude() - (-65)) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 31) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 98.35) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 960.17) < 0.01);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 22) < 0.01);
        assertEquals(impl.getConfidence(), 23);

        data = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtGeographicalInformationImpl);
        impl = (ExtGeographicalInformationImpl)result.getResult();

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid);
        assertTrue(Math.abs(impl.getLatitude() - (-65.5)) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 31.3) < 0.01);
        assertEquals(impl.getAltitude(), -17);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 45.60) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 487.85) < 0.01);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 28) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintyAltitude() - 27.97) < 0.01);
        assertEquals(impl.getConfidence(), 29);

        data = getEncodedData_EllipsoidArc();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtGeographicalInformationImpl);
        impl = (ExtGeographicalInformationImpl)result.getResult();

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidArc);
        assertTrue(Math.abs(impl.getLatitude() - 1) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 171.3) < 0.01);
        assertEquals(impl.getInnerRadius(), 6000);
        assertTrue(Math.abs(impl.getUncertaintyRadius() - 13.58) < 0.01);
        assertTrue(Math.abs(impl.getOffsetAngle() - 22) < 0.01);
        assertTrue(Math.abs(impl.getIncludedAngle() - 24) < 0.01);
        assertEquals(impl.getConfidence(), 39);

        data = getEncodedData_EllipsoidPoint();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtGeographicalInformationImpl);
        impl = (ExtGeographicalInformationImpl)result.getResult();

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertTrue(Math.abs(impl.getLatitude() - 0) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-3)) < 0.01); // -177
    }

    @Test(groups = { "functional.encode", "lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtGeographicalInformationImpl.class);
    	
        ExtGeographicalInformationImpl impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        byte[] data = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyEllipse, -65, 31, 0, 100, 1000, 22, 23, 0, 0, 0, 0, 0, 0);
        data = getEncodedData_EllipsoidPointWithUncertaintyEllipse();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid, -65.5, 31.3, 28, 50, 500, 28, 29, -17, 29, 0, 0, 0, 0);
        data = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidArc, 1, 171.3, 0, 0, 0, 0, 39, 0, 0, 6000, 15, 22, 24);
        data = getEncodedData_EllipsoidArc();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPoint, 0, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        data = getEncodedData_EllipsoidPoint();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}