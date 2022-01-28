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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CSGId;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationInformationTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 22, 2, 1, 0, -127, 6, -111, 0, 32, 34, 17, -15, -93, 9, -128, 7, 82, -16, 16, 0, 111, 8, -92 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 32, 2, 1, 0, -127, 6, -111, 0, 32, 34, 17, -15, -126, 8, -124, -105, 8, 2, -105, 1, 32, 0,
                -93, 9, -128, 7, 82, -16, 16, 0, 111, 8, -92 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 87, 2, 1, 3, -128, 8, 16, 12, 13, 14, 15, 16, 17, 18, -127, 6, -111, 0, 32, 34, 17, -15, -126,
                8, -124, -105, 8, 2, -105, 1, 32, 0, -93, 9, -128, 7, 82, -16, 16, 0, 111, 8, -92, -123, 3, 21, 22, 23,
                -122, 6, -111, -120, 40, 34, 102, -10, -121, 10, 31, 16, 33, 34, 35, 36, 37, 38, 39, 40, -120, 0, -119, 0, -86,
                5, -123, 0, -122, 1, 7, -85, 7, -128, 5, 5, -128, 0, 0, 32 };
    }

    private byte[] getDataGeographicalInformation() {
        return new byte[] { 16, 12, 13, 14, 15, 16, 17, 18 };
    }

    private byte[] getDataLSAIdentity() {
        return new byte[] { 21, 22, 23 };
    }

    private byte[] getDataGeodeticInformation() {
        return new byte[] { 31, 16, 33, 34, 35, 36, 37, 38, 39, 40 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInformationImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationImpl);
        LocationInformationImpl impl = (LocationInformationImpl)result.getResult();
        
        assertEquals((int) impl.getAgeOfLocationInformation(), 0);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 2212);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
        assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationImpl);
        impl = (LocationInformationImpl)result.getResult();

        assertEquals((int) impl.getAgeOfLocationInformation(), 0);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 2212);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
        assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));
        LocationNumber ln = impl.getLocationNumber().getLocationNumber();
        assertEquals(ln.getNatureOfAddressIndicator(), 4);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), 1);
        assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(ln.getScreeningIndicator(), 3);
        assertFalse(impl.getCurrentLocationRetrieved());
        assertFalse(impl.getSaiPresent());

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationImpl);
        impl = (LocationInformationImpl)result.getResult();

        assertEquals((int) impl.getAgeOfLocationInformation(), 3);
        assertTrue(ByteBufUtil.equals(impl.getGeographicalInformation().getValue(), Unpooled.wrappedBuffer(getDataGeographicalInformation())));
        assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));
        ln = impl.getLocationNumber().getLocationNumber();
        assertEquals(ln.getNatureOfAddressIndicator(), 4);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), 1);
        assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(ln.getScreeningIndicator(), 3);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 2212);
        assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
        assertTrue(ByteBufUtil.equals(impl.getSelectedLSAId().getValue(), Unpooled.wrappedBuffer(getDataLSAIdentity())));
        assertTrue(impl.getMscNumber().getAddress().equals("888222666"));
        assertTrue(ByteBufUtil.equals(impl.getGeodeticInformation().getValue(), Unpooled.wrappedBuffer(getDataGeodeticInformation())));
        assertTrue(impl.getCurrentLocationRetrieved());
        assertTrue(impl.getSaiPresent());
        assertEquals((int) impl.getLocationInformationEPS().getAgeOfLocationInformation(), 7);
        assertTrue(impl.getCurrentLocationRetrieved());
        CSGId bs = impl.getUserCSGInformation().getCSGId();
        assertTrue(bs.isBitSet(0));
        assertFalse(bs.isBitSet(1));
        assertFalse(bs.isBitSet(25));
        assertTrue(bs.isBitSet(26));
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInformationImpl.class);
    	        
        ISDNAddressStringImpl vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "000222111");
        CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(
                250, 1, 111, 2212);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                cellGlobalIdOrServiceAreaIdFixedLength);
        LocationInformationImpl impl = new LocationInformationImpl(0, null, vlrNumber, null, cellGlobalIdOrServiceAreaIdOrLAI,
                null, null, null, null, false, false, null, null);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000222111");
        cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(250, 1, 111, 2212);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
        LocationNumberMapImpl locationNumber = new LocationNumberMapImpl(new LocationNumberImpl(Unpooled.wrappedBuffer(new byte[] { (byte) 132, (byte) 151, 8, 2, (byte) 151,
                1, 32, 0 })));
        impl = new LocationInformationImpl(0, null, vlrNumber, locationNumber, cellGlobalIdOrServiceAreaIdOrLAI, null, null,
                null, null, false, false, null, null);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        ByteBuf geoBuffer=Unpooled.wrappedBuffer(getDataGeographicalInformation());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geoBuffer), GeographicalInformationImpl.decodeLongitude(geoBuffer), GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
        
        LSAIdentityImpl selectedLSAId = new LSAIdentityImpl(Unpooled.wrappedBuffer(getDataLSAIdentity()));
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "888222666");
        
        ByteBuf geodeticBuffer=Unpooled.wrappedBuffer(getDataGeodeticInformation());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF, GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geodeticBuffer), GeographicalInformationImpl.decodeLongitude(geodeticBuffer), GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),geodeticBuffer.readByte() & 0x0FF);
        
        LocationInformationEPSImpl liEps = new LocationInformationEPSImpl(null, null, null, null, null, true, 7, null);
        CSGIdImpl csgId = new CSGIdImpl();
        csgId.setBit(0);
        csgId.setBit(26);
        UserCSGInformationImpl uci = new UserCSGInformationImpl(csgId, null, null, null);
        impl = new LocationInformationImpl(3, ggi, vlrNumber, locationNumber, cellGlobalIdOrServiceAreaIdOrLAI, null,
                selectedLSAId, mscNumber, gdi, true, true, liEps, uci);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
