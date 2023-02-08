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
package org.restcomm.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.EsiGprs.PdpContextChangeOfPositionSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class EventReportGPRSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 72, -128, 1, 2, -95, 3, -128, 1, 1, -94, 59, -96, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92,
                -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 16, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3,
                91, 92, 93, -122, 0, -121, 10, 1, 16, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -125, 1, 1 };
    };

    public byte[] getDataLiveTrace() {
        return new byte[] { 0x30, 42, (byte) 0x80, 0x01, 0x0e, (byte) 0xa1, 0x03, (byte) 0x80, 0x01, 0x00, (byte) 0xa2, 32,
                (byte) 0xa1, 30, (byte) 0xa2,/* 0x1a, */28, -96, 9 /* end */, (byte) 0x80, 0x07, 0x27, (byte) 0xf4, 0x43, 0x08,
                (byte) 0xba, 0x16, 0x4e, (byte) 0x81, 0x06, 0x27, (byte) 0xf4, 0x43, 0x08, (byte) 0xba, 0x00, (byte) 0x83,
                0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26, (byte) 0x99, 0x59 };
    };

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
        return new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventReportGPRSRequestImpl);
        
        EventReportGPRSRequestImpl prim = (EventReportGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
        assertNotNull(prim.getMiscGPRSInfo().getMessageType());
        assertNull(prim.getMiscGPRSInfo().getDpAssignment());
        assertEquals(prim.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);

        assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI()
                .getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI()
                .getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI()
                .getLAIFixedLength().getLac(), 4444);

        assertEquals(prim.getPDPID().getId(), 1);
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getDataLiveTrace();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventReportGPRSRequestImpl);
        
        EventReportGPRSRequestImpl prim = (EventReportGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getGPRSEventType(), GPRSEventType.pdpContextChangeOfPosition);
        assertNotNull(prim.getMiscGPRSInfo().getMessageType());
        assertNull(prim.getMiscGPRSInfo().getDpAssignment());
        assertEquals(prim.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.request);

        assertNotNull(prim.getGPRSEventSpecificInformation().getPdpContextChangeOfPositionSpecificInformation()
                .getLocationInformationGPRS());
        ISDNAddressString sgsn = prim.getGPRSEventSpecificInformation().getPdpContextChangeOfPositionSpecificInformation()
                .getLocationInformationGPRS().getSGSNNumber();
        assertTrue(sgsn.getAddress().equals("553496629995"));

        assertNull(prim.getGPRSEventSpecificInformation().getPdpContextChangeOfPositionSpecificInformation()
                .getQualityOfService());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportGPRSRequestImpl.class);
    	
        GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
        MiscCallInfoImpl miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);

        // gprsEventSpecificInformation
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(this.getEncodedDataRAIdentity()));
        
        ByteBuf geoBuffer=Unpooled.wrappedBuffer(getGeographicalInformation());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geoBuffer), GeographicalInformationImpl.decodeLongitude(geoBuffer), GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
        
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(this.getEncodedDataLSAIdentity()));
        
        ByteBuf geodeticBuffer=Unpooled.wrappedBuffer(getGeodeticInformation());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF, GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geodeticBuffer), GeographicalInformationImpl.decodeLongitude(geodeticBuffer), GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),geodeticBuffer.readByte() & 0x0FF);
        
        LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true,
                gdi, true, 13);
        GPRSEventSpecificInformationImpl gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
                locationInformationGPRS);

        // pdpID
        PDPIDImpl pdpID = new PDPIDImpl(1);

        EventReportGPRSRequestImpl prim = new EventReportGPRSRequestImpl(gprsEventType, miscGPRSInfo,
                gprsEventSpecificInformation, pdpID);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportGPRSRequestImpl.class);
    	
        GPRSEventType gprsEventType = GPRSEventType.pdpContextChangeOfPosition;
        MiscCallInfoImpl miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);

        // gprsEventSpecificInformation
        CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(724,34,0x08ba,0x164e);                

        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                cellGlobalIdOrServiceAreaIdFixedLength);
        RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 0x27, (byte) 0xf4, 0x43, 0x08, (byte) 0xba, 0x00 }));
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629995");
        LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, null, sgsn, null, null,
                false, null, false, null);

        PdpContextChangeOfPositionSpecificInformationImpl pdpContextchangeOfPositionSpecificInformation = new PdpContextChangeOfPositionSpecificInformationImpl(
                null, null, locationInformationGPRS, null, null, null, null);
        GPRSEventSpecificInformationImpl gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
                pdpContextchangeOfPositionSpecificInformation);

        EventReportGPRSRequestImpl prim = new EventReportGPRSRequestImpl(gprsEventType, miscGPRSInfo,
                gprsEventSpecificInformation, null);
        byte[] rawData = this.getDataLiveTrace();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
