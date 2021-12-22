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
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationInformationGPRSTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33,
                34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, -120, 0, -119, 1, 13 };
    }

    private byte[] getEncodedData_2() {
        return new byte[] { 48, 55, (byte) 128, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33,
                34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8,
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
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getEncodedDataLSAIdentity() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
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
        assertTrue(Arrays.equals(impl.getRouteingAreaIdentity().getData(), this.getEncodedDataRAIdentity()));
        assertTrue(Arrays.equals(impl.getGeographicalInformation().getData(), this.getGeographicalInformation()));
        assertTrue(impl.getSGSNNumber().getAddress().equals("654321"));
        assertTrue(Arrays.equals(impl.getLSAIdentity().getData(), this.getEncodedDataLSAIdentity()));
        assertTrue(impl.isSaiPresent());
        assertTrue(Arrays.equals(impl.getGeodeticInformation().getData(), this.getGeodeticInformation()));
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
        assertTrue(Arrays.equals(impl.getRouteingAreaIdentity().getData(), this.getEncodedDataRAIdentity()));
        assertTrue(Arrays.equals(impl.getGeographicalInformation().getData(), this.getGeographicalInformation()));
        assertTrue(impl.getSGSNNumber().getAddress().equals("654321"));
        assertTrue(Arrays.equals(impl.getLSAIdentity().getData(), this.getEncodedDataLSAIdentity()));
        assertTrue(impl.isSaiPresent());
        assertTrue(Arrays.equals(impl.getGeodeticInformation().getData(), this.getGeodeticInformation()));
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
        RAIdentityImpl ra = new RAIdentityImpl(this.getEncodedDataRAIdentity());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(this.getEncodedDataLSAIdentity());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());

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