/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.isup.BearerCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LowLayerCompatibilityImpl;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInapImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUDataImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
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
public class InitialDPArgExtensionTest {

    public byte[] getData1() {
        return new byte[] { 48, 8, (byte) 129, 6, (byte) 145, 34, 112, 87, 0, 112 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 12, (byte) 128, 4, (byte) 152, 17, 17, 17, (byte) 129, 4, 1, 16, 34, 34 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 59, (byte) 130, 3, 11, 12, 13, (byte) 131, 6, 0, 0, 16, 33, 67, (byte) 245, (byte) 132, 2, 5, (byte) 224, (byte) 133, 2, 5, 32,
                (byte) 166, 4, (byte) 128, 2, (byte) 160, (byte) 128, (byte) 167, 3, (byte) 130, 1, 38, (byte) 136, 2, (byte) 160, (byte) 128,
                (byte) 137, 4, 31, 32, 33, 34, (byte) 138, 4, 41, 42, 43, 44, (byte) 139, 0, (byte) 172, 3, (byte) 128, 1, (byte) 129 ,(byte) 141, 0, (byte) 142, 0
        };
    }

    public byte[] getMSClassmark2Data() {
        return new byte[] { 11, 12, 13 };
    }

    public byte[] getLowLayerCompatibilityData() {
        return new byte[] { 31, 32, 33, 34 };
    }

    public byte[] getLowLayerCompatibility2Data() {
        return new byte[] { 41, 42, 43, 44 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDPArgExtensionImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPArgExtensionImpl);
        
        InitialDPArgExtensionImpl elem = (InitialDPArgExtensionImpl)result.getResult(); 
        elem.patchVersion(2);
        assertEquals(elem.getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getGmscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getGmscAddress().getAddress().equals("2207750007"));
        assertNull(elem.getForwardingDestinationNumber());

        assertNull(elem.getMSClassmark2());
        assertNull(elem.getIMEI());
        assertNull(elem.getSupportedCamelPhases());
        assertNull(elem.getOfferedCamel4Functionalities());
        assertNull(elem.getBearerCapability2());
        assertNull(elem.getExtBasicServiceCode2());
        assertNull(elem.getHighLayerCompatibility2());
        assertNull(elem.getLowLayerCompatibility());
        assertNull(elem.getLowLayerCompatibility2());
        assertFalse(elem.getEnhancedDialledServicesAllowed());
        assertNull(elem.getUUData());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPArgExtensionImpl);
        
        elem = (InitialDPArgExtensionImpl)result.getResult(); 
        elem.patchVersion(4);
        assertEquals(elem.getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getGmscAddress().getNumberingPlan(), NumberingPlan.national);
        assertTrue(elem.getGmscAddress().getAddress().equals("111111"));
        CalledPartyNumber cpn = elem.getForwardingDestinationNumber().getCalledPartyNumber();
        assertTrue(cpn.getAddress().equals("2222"));
        assertEquals(cpn.getInternalNetworkNumberIndicator(), 0);
        assertEquals(cpn.getNatureOfAddressIndicator(), 1);
        assertEquals(cpn.getNumberingPlanIndicator(), 1);

        assertNull(elem.getMSClassmark2());
        assertNull(elem.getIMEI());
        assertNull(elem.getSupportedCamelPhases());
        assertNull(elem.getOfferedCamel4Functionalities());
        assertNull(elem.getBearerCapability2());
        assertNull(elem.getExtBasicServiceCode2());
        assertNull(elem.getHighLayerCompatibility2());
        assertNull(elem.getLowLayerCompatibility());
        assertNull(elem.getLowLayerCompatibility2());
        assertFalse(elem.getEnhancedDialledServicesAllowed());
        assertNull(elem.getUUData());
        assertFalse(elem.getCollectInformationAllowed());
        assertFalse(elem.getReleaseCallArgExtensionAllowed());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPArgExtensionImpl);
        
        elem = (InitialDPArgExtensionImpl)result.getResult(); 
        elem.patchVersion(4);        
        assertNull(elem.getGmscAddress());
        assertNull(elem.getForwardingDestinationNumber());
        assertEquals(elem.getMSClassmark2().getData(), getMSClassmark2Data());
        assertEquals(elem.getIMEI().getIMEI(), "00000112345");
        assertTrue(elem.getSupportedCamelPhases().getPhase1Supported());
        assertTrue(elem.getSupportedCamelPhases().getPhase2Supported());
        assertTrue(elem.getSupportedCamelPhases().getPhase3Supported());
        assertFalse(elem.getSupportedCamelPhases().getPhase4Supported());
        assertTrue(elem.getOfferedCamel4Functionalities().getMoveLeg());
        assertFalse(elem.getOfferedCamel4Functionalities().getChangeOfPositionDP());
        assertEquals(elem.getBearerCapability2().getBearerCap().getUserServiceInformation().getCodingStandart(), UserServiceInformation._CS_INTERNATIONAL);
        assertEquals(elem.getExtBasicServiceCode2().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
        assertEquals(elem.getHighLayerCompatibility2().getHighLayerCompatibility().getCodingStandard(), UserTeleserviceInformation._CODING_STANDARD_ISO_IEC);
        assertEquals(elem.getLowLayerCompatibility().getData(), getLowLayerCompatibilityData());
        assertEquals(elem.getLowLayerCompatibility2().getData(), getLowLayerCompatibility2Data());
        assertTrue(elem.getEnhancedDialledServicesAllowed());
        assertEquals(elem.getUUData().getUUIndicator().getData(), 129);
        assertTrue(elem.getCollectInformationAllowed());
        assertTrue(elem.getReleaseCallArgExtensionAllowed());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDPArgExtensionImpl.class);
    	
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        InitialDPArgExtensionImpl elem = new InitialDPArgExtensionImpl(null, gmscAddress);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.national, "111111");
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "2222", 1, 0);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        elem = new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, null, null, null, null, null, null,
                null, null, null, false, null, false, false);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        MSClassmark2Impl msClassmark2 = new MSClassmark2Impl(getMSClassmark2Data());
        IMEIImpl imei = new IMEIImpl("00000112345");
        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        // boolean phase1, boolean phase2, boolean phase3, boolean phase4
        OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl(false, false, true, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false);

        UserServiceInformation userServiceInformation = new UserServiceInformationImpl();
        userServiceInformation.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        BearerCapImpl bearerCap = new BearerCapImpl(userServiceInformation);
        BearerCapabilityImpl bearerCapability2 = new BearerCapabilityImpl(bearerCap);

        ExtBearerServiceCodeImpl extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extBearerService);
        UserTeleserviceInformation userTeleserviceInformation = new UserTeleserviceInformationImpl();
        userTeleserviceInformation.setCodingStandard(UserTeleserviceInformation._CODING_STANDARD_ISO_IEC);
        HighLayerCompatibilityInapImpl highLayerCompatibility2 = new HighLayerCompatibilityInapImpl(userTeleserviceInformation);
        LowLayerCompatibilityImpl lowLayerCompatibility = new LowLayerCompatibilityImpl(getLowLayerCompatibilityData());
        LowLayerCompatibilityImpl lowLayerCompatibility2 = new LowLayerCompatibilityImpl(getLowLayerCompatibility2Data());
        UUIndicatorImpl uuIndicator = new UUIndicatorImpl(129);
        UUDataImpl uuData = new UUDataImpl(uuIndicator, null, false, null);
        elem = new InitialDPArgExtensionImpl(null, null, msClassmark2, imei, supportedCamelPhases, offeredCamel4Functionalities, bearerCapability2,
                extBasicServiceCode2, highLayerCompatibility2, lowLayerCompatibility, lowLayerCompatibility2, true, uuData, true, true);
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // ISDNAddressString gmscAddress, CalledPartyNumberCap forwardingDestinationNumber, MSClassmark2 msClassmark2, IMEI
        // imei,
        // SupportedCamelPhases supportedCamelPhases, OfferedCamel4Functionalities offeredCamel4Functionalities,
        // BearerCapability bearerCapability2,
        // ExtBasicServiceCode extBasicServiceCode2, HighLayerCompatibilityInap highLayerCompatibility2, LowLayerCompatibility
        // lowLayerCompatibility,
        // LowLayerCompatibility lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUData uuData, boolean
        // isCAPVersion3orLater
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {

        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "2222", 1, 0);
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        InitialDPArgExtensionImpl original = new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, null,
                null, null, null, null, null, null, null, null, false, null, false, false, false);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "initialDPArgExtension", InitialDPArgExtensionImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InitialDPArgExtensionImpl copy = reader.read("initialDPArgExtension", InitialDPArgExtensionImpl.class);

        assertEquals(copy.getGmscAddress().getAddress(), original.getGmscAddress().getAddress());
        assertEquals(copy.getForwardingDestinationNumber().getCalledPartyNumber().getAddress(), original
                .getForwardingDestinationNumber().getCalledPartyNumber().getAddress());
        assertNull(copy.getMSClassmark2());
        assertNull(copy.getIMEI());
        assertNull(copy.getSupportedCamelPhases());
        assertNull(copy.getOfferedCamel4Functionalities());
        assertNull(copy.getBearerCapability2());
        assertNull(copy.getExtBasicServiceCode2());
        assertNull(copy.getHighLayerCompatibility2());
        assertNull(copy.getLowLayerCompatibility());
        assertNull(copy.getLowLayerCompatibility2());
        assertFalse(copy.getEnhancedDialledServicesAllowed());
        assertNull(copy.getUUData());
        assertEquals(copy.getCollectInformationAllowed(), original.getCollectInformationAllowed());
        assertEquals(copy.getReleaseCallArgExtensionAllowed(), original.getReleaseCallArgExtensionAllowed());


        MSClassmark2 msClassmark2 = new MSClassmark2Impl(getMSClassmark2Data());
        IMEI imei = new IMEIImpl("00000112345");
        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        // boolean phase1, boolean phase2, boolean phase3, boolean phase4
        OfferedCamel4Functionalities offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl(false, false, true, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false);

        UserServiceInformation userServiceInformation = new UserServiceInformationImpl();
        userServiceInformation.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        BearerCap bearerCap = new BearerCapImpl(userServiceInformation);
        BearerCapability bearerCapability2 = new BearerCapabilityImpl(bearerCap);

        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extBearerService);
        UserTeleserviceInformation userTeleserviceInformation = new UserTeleserviceInformationImpl();
        userTeleserviceInformation.setCodingStandard(UserTeleserviceInformation._CODING_STANDARD_ISO_IEC);
        HighLayerCompatibilityInap highLayerCompatibility2 = new HighLayerCompatibilityInapImpl(userTeleserviceInformation);
        LowLayerCompatibility lowLayerCompatibility = new LowLayerCompatibilityImpl(getLowLayerCompatibilityData());
        LowLayerCompatibility lowLayerCompatibility2 = new LowLayerCompatibilityImpl(getLowLayerCompatibility2Data());
        UUIndicator uuIndicator = new UUIndicatorImpl(129);
        UUData uuData = new UUDataImpl(uuIndicator, null, false, null);
        original = new InitialDPArgExtensionImpl(null, null, msClassmark2, imei, supportedCamelPhases, offeredCamel4Functionalities, bearerCapability2,
                extBasicServiceCode2, highLayerCompatibility2, lowLayerCompatibility, lowLayerCompatibility2, true, uuData, true, true, true);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "initialDPArgExtension", InitialDPArgExtensionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("initialDPArgExtension", InitialDPArgExtensionImpl.class);

        assertNull(copy.getGmscAddress());
        assertNull(copy.getForwardingDestinationNumber());

        assertEquals(copy.getMSClassmark2().getData(), original.getMSClassmark2().getData());
        assertEquals(copy.getIMEI().getIMEI(), original.getIMEI().getIMEI());
        assertEquals(copy.getSupportedCamelPhases().getPhase1Supported(), original.getSupportedCamelPhases().getPhase1Supported());
        assertEquals(copy.getSupportedCamelPhases().getPhase4Supported(), original.getSupportedCamelPhases().getPhase4Supported());
        assertEquals(copy.getOfferedCamel4Functionalities().getMoveLeg(), original.getOfferedCamel4Functionalities().getMoveLeg());
        assertEquals(copy.getOfferedCamel4Functionalities().getChangeOfPositionDP(), original.getOfferedCamel4Functionalities().getChangeOfPositionDP());
        assertEquals(copy.getBearerCapability2().getBearerCap().getUserServiceInformation().getCodingStandart(), original.getBearerCapability2().getBearerCap()
                .getUserServiceInformation().getCodingStandart());
        assertEquals(copy.getExtBasicServiceCode2().getExtBearerService().getBearerServiceCodeValue(), original.getExtBasicServiceCode2().getExtBearerService()
                .getBearerServiceCodeValue());
        assertEquals(copy.getHighLayerCompatibility2().getHighLayerCompatibility().getCodingStandard(), original.getHighLayerCompatibility2()
                .getHighLayerCompatibility().getCodingStandard());
        assertEquals(copy.getLowLayerCompatibility().getData(), original.getLowLayerCompatibility().getData());
        assertEquals(copy.getLowLayerCompatibility2().getData(), original.getLowLayerCompatibility2().getData());
        assertEquals(copy.getEnhancedDialledServicesAllowed(), original.getEnhancedDialledServicesAllowed());
        assertEquals(copy.getUUData().getUUIndicator().getData(), original.getUUData().getUUIndicator().getData());
        assertEquals(copy.getCollectInformationAllowed(), original.getCollectInformationAllowed());
        assertEquals(copy.getReleaseCallArgExtensionAllowed(), original.getReleaseCallArgExtensionAllowed());
    }*/
}
