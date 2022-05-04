/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
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
public class SubscriberLocationReportRequestTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        return new byte[] { 48, -127, -78, 10, 1, 0, 48, 3, -128, 1, 2, 48, 8, 4, 6, -111, 68, 68, 84, 85, 85, -128, 6, -111, 102, 102, 118, 119, 119, -127, 5, 33, 67, 21, 50, 84, -126, 8, 33, 67, 101, -121, 9, 33, 67, 101, -125, 6, -111, -120, -120, -104, -103, -103, -124, 6, -111, -120, -120, 8, 0, 0, -123, 8, 16, 92, 113, -57, -106, 11, 97, 7, -122, 1, 5, -89, 4, -95, 2, -128, 0, -120, 8, 16, 92, 113, -57, -106, 11, 97, 7, -87, 14, 3, 2, 7, -128, -95, 8, 4, 6, -111, 68, 68, 84, 85, 85, -118, 1, 6, -117, 2, 13, 14, -116, 3, 15, 16, 17, -83, 7, -127, 5, 34, -16, 33, 16, -31, -114, 5, 4, 22, 23, 24, 25, -113, 1, 7, -111, 0, -110, 0, -109, 1, 0, -108, 4, 0, 90, 0, 59, -107, 1, 9, -74, 6, 2, 1, 10, 2, 1, 11, -105, 0, -104, 2, 31, 32, -103, 1, 33, -70, 8, -128, 6, -111, -111, -126, 115, 100, -11 };
    }

    public byte[] getDataExtGeographicalInformation() {
        return new byte[] { 11 };
    }

    public byte[] getDataAddGeographicalInformation() {
        return new byte[] { 12 };
    }

    public byte[] getPositioningDataInformation() {
        return new byte[] { 13, 14 };
    }

    public byte[] getUtranPositioningDataInfo() {
        return new byte[] { 15, 16, 17 };
    }

    public byte[] getGSNAddress() {
        return new byte[] { 22, 23, 24, 25 };
    }

    public byte[] getVelocityEstimate() {
        return new byte[] { 26, 27, 28, 29 };
    }

    public byte[] getGeranGANSSpositioningData() {
        return new byte[] { 31, 32 };
    }

    public byte[] getUtranGANSSpositioningData() {
        return new byte[] { 33 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SubscriberLocationReportRequestImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberLocationReportRequestImpl);
        SubscriberLocationReportRequestImpl imp = (SubscriberLocationReportRequestImpl)result.getResult();

        assertEquals(imp.getLCSEvent(), LCSEvent.emergencyCallOrigination);
        assertEquals(imp.getLCSClientID().getLCSClientType(), LCSClientType.plmnOperatorServices);
        assertTrue(imp.getLCSLocationInfo().getNetworkNodeNumber().getAddress().equals("4444455555"));
        assertTrue(imp.getMSISDN().getAddress().equals("6666677777"));
        assertTrue(imp.getIMSI().getData().equals("1234512345"));
        assertTrue(imp.getIMEI().getIMEI().equals("1234567890123456"));
        assertTrue(imp.getNaESRD().getAddress().equals("8888899999"));
        assertTrue(imp.getNaESRK().getAddress().equals("8888800000"));
        
        assertEquals(imp.getLocationEstimate().getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(imp.getLocationEstimate().getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(imp.getLocationEstimate().getLongitude() - (-149)) < 0.01);  // -31
        assertTrue(Math.abs(imp.getLocationEstimate().getUncertainty() - 9.48) < 0.01);
        
        assertEquals((int) imp.getAgeOfLocationEstimate(), 5);
        assertTrue(imp.getSLRArgExtensionContainer().getSlrArgPcsExtensions().getNaEsrkRequest());
        
        assertEquals(imp.getAdditionalLocationEstimate().getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(imp.getAdditionalLocationEstimate().getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(imp.getAdditionalLocationEstimate().getLongitude() - (-149)) < 0.01); // -31
        assertTrue(Math.abs(imp.getAdditionalLocationEstimate().getUncertainty() - 9.48) < 0.01);
        
        assertTrue(imp.getDeferredmtlrData().getDeferredLocationEventType().getMsAvailable());
        assertFalse(imp.getDeferredmtlrData().getDeferredLocationEventType().getEnteringIntoArea());
        assertFalse(imp.getDeferredmtlrData().getDeferredLocationEventType().getLeavingFromArea());
        assertFalse(imp.getDeferredmtlrData().getDeferredLocationEventType().getBeingInsideArea());
        assertEquals((int) imp.getLCSReferenceNumber(), 6);
        assertTrue(ByteBufUtil.equals(imp.getGeranPositioningData().getValue(),Unpooled.wrappedBuffer(getPositioningDataInformation())));
        assertTrue(ByteBufUtil.equals(imp.getUtranPositioningData().getValue(),Unpooled.wrappedBuffer(getUtranPositioningDataInfo())));
        assertEquals(imp.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 220);
        assertEquals(imp.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 12);
        assertEquals(imp.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4321);
        
        assertEquals(imp.getHGMLCAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(imp.getHGMLCAddress().getGSNAddressData(),Unpooled.wrappedBuffer(getGSNAddress())));
        
        assertEquals((int) imp.getLCSServiceTypeID(), 7);
        assertTrue(imp.getSaiPresent());
        assertTrue(imp.getPseudonymIndicator());
        assertEquals(imp.getAccuracyFulfilmentIndicator(), AccuracyFulfilmentIndicator.requestedAccuracyFulfilled);
        
        assertEquals(imp.getVelocityEstimate().getVelocityType(), VelocityType.HorizontalVelocity);
        assertEquals(imp.getVelocityEstimate().getHorizontalSpeed(), 59);
        assertEquals(imp.getVelocityEstimate().getBearing(), 90);
        
        assertEquals((int) imp.getSequenceNumber(), 9);
        assertEquals((int) imp.getPeriodicLDRInfo().getReportingAmount(), 10);
        assertEquals((int) imp.getPeriodicLDRInfo().getReportingInterval(), 11);
        assertTrue(imp.getMoLrShortCircuitIndicator());
        assertTrue(ByteBufUtil.equals(imp.getGeranGANSSpositioningData().getValue(), Unpooled.wrappedBuffer(getGeranGANSSpositioningData())));
        assertTrue(ByteBufUtil.equals(imp.getUtranGANSSpositioningData().getValue(), Unpooled.wrappedBuffer(getUtranGANSSpositioningData())));
        assertTrue(imp.getTargetServingNodeForHandover().getMscNumber().getAddress().equals("192837465"));

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SubscriberLocationReportRequestImpl.class);
    	
        byte[] data = getEncodedData();

        LCSClientIDImpl lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null, null, null, null, null,
                null);
        ISDNAddressStringImpl networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "4444455555");
        LCSLocationInfoImpl lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, false, null, null, null,
                null, null);
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "6666677777");
        ;
        IMSIImpl imsi = new IMSIImpl("1234512345");
        IMEIImpl imei = new IMEIImpl("1234567890123456");
        ISDNAddressStringImpl naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "8888899999");
        ISDNAddressStringImpl naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "8888800000");
        ExtGeographicalInformationImpl locationEstimate = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        SLRArgPCSExtensionsImpl slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(true);
        SLRArgExtensionContainerImpl slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
        AddGeographicalInformationImpl addLocationEstimate = new AddGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        DeferredLocationEventTypeImpl deferredLocationEventType = new DeferredLocationEventTypeImpl(true, false, false, false);
        // boolean msAvailable, boolean enteringIntoArea, boolean leavingFromArea, boolean beingInsideArea
        DeferredmtlrDataImpl deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, null, lcsLocationInfo);
        PositioningDataInformationImpl geranPositioningData = new PositioningDataInformationImpl(Unpooled.wrappedBuffer(
                getPositioningDataInformation()));
        UtranPositioningDataInfoImpl utranPositioningData = new UtranPositioningDataInfoImpl(Unpooled.wrappedBuffer(getUtranPositioningDataInfo()));
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(220, 12, 4321);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
        GSNAddressImpl hgmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getGSNAddress()));
        VelocityEstimateImpl velocityEstimate = new VelocityEstimateImpl(VelocityType.HorizontalVelocity, 59, 90, 0, 0, 0);;
        PeriodicLDRInfoImpl periodicLDRInfo = new PeriodicLDRInfoImpl(10, 11);
        GeranGANSSpositioningDataImpl geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(Unpooled.wrappedBuffer(
                getGeranGANSSpositioningData()));
        UtranGANSSpositioningDataImpl utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(Unpooled.wrappedBuffer(
                getUtranGANSSpositioningData()));
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "192837465");
        ServingNodeAddressImpl targetServingNodeForHandover = new ServingNodeAddressImpl(mscNumber, true);

        SubscriberLocationReportRequestImpl imp = new SubscriberLocationReportRequestImpl(LCSEvent.emergencyCallOrigination,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, 5,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, 6, geranPositioningData, utranPositioningData,
                cellIdOrSai, hgmlcAddress, 7, true, true, AccuracyFulfilmentIndicator.requestedAccuracyFulfilled,
                velocityEstimate, 9, periodicLDRInfo, true, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover);
        
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}