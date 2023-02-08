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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
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
public class LocationInformationGPRSTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 16, 32, 33,
                34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 16, 3, 4, 5, 6, 7, 8,
                9, 10, -120, 0, -119, 1, 13 };
    }

    private byte[] getEncodedData_2() {
        return new byte[] { 48, 55, (byte) 128, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 16, 32, 33,
                34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 16, 3, 4, 5, 6, 7, 8,
                9, 10, -120, 0, -119, 1, 13 };
    }

    private byte[] getEncodedData_3() {
        return new byte[] { 48, 11, -96, 9, -128, 7, 82, -15, 32, 17, 93, 12, -6 };
    }

    private byte[] getEncodedData_4() {
        return new byte[] { 48, 9, (byte) 128, 7, 82, -15, 32, 17, 93, 12, -6 };
    }

    private byte[] getEncodedDataRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getGeographicalInformation() {
        return new byte[] { 16, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getEncodedDataLSAIdentity() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInformationGPRSImpl.class);
    	            	
        byte[] rawData = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationGPRSImpl);
        LocationInformationGPRSImpl impl = (LocationInformationGPRSImpl)result.getResult();
        
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);
        assertTrue(ByteBufUtil.equals(impl.getRouteingAreaIdentity().getValue(), Unpooled.wrappedBuffer(this.getEncodedDataRAIdentity())));
        assertTrue(ByteBufUtil.equals(impl.getGeographicalInformation().getValue(), Unpooled.wrappedBuffer(this.getGeographicalInformation())));
        assertTrue(impl.getSGSNNumber().getAddress().equals("654321"));
        assertTrue(ByteBufUtil.equals(impl.getLSAIdentity().getValue(), Unpooled.wrappedBuffer(this.getEncodedDataLSAIdentity())));
        assertTrue(impl.isSaiPresent());
        assertTrue(ByteBufUtil.equals(impl.getGeodeticInformation().getValue(), Unpooled.wrappedBuffer(this.getGeodeticInformation())));
        assertTrue(impl.isCurrentLocationRetrieved());
        assertEquals((int) impl.getAgeOfLocationInformation(), 13);

        rawData = getEncodedData_2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationGPRSImpl);
        impl = (LocationInformationGPRSImpl)result.getResult();

        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);
        assertTrue(ByteBufUtil.equals(impl.getRouteingAreaIdentity().getValue(), Unpooled.wrappedBuffer(this.getEncodedDataRAIdentity())));
        assertTrue(ByteBufUtil.equals(impl.getGeographicalInformation().getValue(), Unpooled.wrappedBuffer(this.getGeographicalInformation())));
        assertTrue(impl.getSGSNNumber().getAddress().equals("654321"));
        assertTrue(ByteBufUtil.equals(impl.getLSAIdentity().getValue(), Unpooled.wrappedBuffer(this.getEncodedDataLSAIdentity())));
        assertTrue(impl.isSaiPresent());
        assertTrue(ByteBufUtil.equals(impl.getGeodeticInformation().getValue(), Unpooled.wrappedBuffer(this.getGeodeticInformation())));
        assertTrue(impl.isCurrentLocationRetrieved());
        assertEquals((int) impl.getAgeOfLocationInformation(), 13);

        rawData = getEncodedData_3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationGPRSImpl);
        impl = (LocationInformationGPRSImpl)result.getResult();

        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 251);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 2);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 4445);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 3322);
        assertNull(impl.getRouteingAreaIdentity());

        rawData = getEncodedData_4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationGPRSImpl);
        impl = (LocationInformationGPRSImpl)result.getResult();

        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 251);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 2);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 4445);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 3322);
        assertNull(impl.getRouteingAreaIdentity());
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInformationGPRSImpl.class);
    	            	        
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(this.getEncodedDataRAIdentity()));
        
        ByteBuf geoBuffer=Unpooled.wrappedBuffer(getGeographicalInformation());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geoBuffer), GeographicalInformationImpl.decodeLongitude(geoBuffer), GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
        
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(this.getEncodedDataLSAIdentity()));
        
        ByteBuf geodeticBuffer=Unpooled.wrappedBuffer(getGeodeticInformation());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF, GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geodeticBuffer), GeographicalInformationImpl.decodeLongitude(geodeticBuffer), GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),geodeticBuffer.readByte() & 0x0FF);
        
        LocationInformationGPRSImpl impl = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true, gdi, true, 13);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        CellGlobalIdOrServiceAreaIdFixedLengthImpl v1 = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(251, 2, 4445, 3322);
        cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(v1);
        impl = new LocationInformationGPRSImpl(cgi, null, null, null, null, null, false, null, false, null);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData_3();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}