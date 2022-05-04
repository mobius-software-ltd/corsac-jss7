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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.callhandling.UUDataImpl;
import org.restcomm.protocols.ss7.commonapp.callhandling.UUIndicatorImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.LowLayerCompatibilityImpl;
import org.restcomm.protocols.ss7.commonapp.isup.BearerIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
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
public class InitialDPArgExtensionTest {

    public byte[] getData1() {
        return new byte[] { 48, 8, (byte) 129, 6, (byte) 145, 34, 112, 87, 0, 112 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 12, (byte) 128, 4, (byte) 152, 17, 17, 17, (byte) 129, 4, 1, 16, 34, 34 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 60, (byte) 130, 3, 11, 12, 13, (byte) 131, 6, 0, 0, 16, 33, 67, (byte) 245, (byte) 132, 2, 5, (byte) 224, (byte) 133, 3, 1, 32, 
        		0, (byte) 166, 4, (byte) 128, 2, (byte) 160, (byte) 128, (byte) 167, 3, (byte) 130, 1, 38, (byte) 136, 2, (byte) 160, (byte) 128,
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
    	parser.replaceClass(InitialDPArgExtensionV1Impl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPArgExtensionV1Impl);
        
        InitialDPArgExtension elem = (InitialDPArgExtensionV1Impl)result.getResult(); 
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

        parser=new ASNParser(true);
    	parser.replaceClass(InitialDPArgExtensionV3Impl.class);
    	
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPArgExtensionV3Impl);
        
        elem = (InitialDPArgExtensionV3Impl)result.getResult(); 
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
        assertTrue(result.getResult() instanceof InitialDPArgExtensionV3Impl);
        
        elem = (InitialDPArgExtensionV3Impl)result.getResult(); 
        assertNull(elem.getGmscAddress());
        assertNull(elem.getForwardingDestinationNumber());
        assertTrue(ByteBufUtil.equals(elem.getMSClassmark2().getValue(), Unpooled.wrappedBuffer(getMSClassmark2Data())));
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
        assertTrue(ByteBufUtil.equals(elem.getLowLayerCompatibility().getValue(), Unpooled.wrappedBuffer(getLowLayerCompatibilityData())));
        assertTrue(ByteBufUtil.equals(elem.getLowLayerCompatibility2().getValue(), Unpooled.wrappedBuffer(getLowLayerCompatibility2Data())));
        assertTrue(elem.getEnhancedDialledServicesAllowed());
        assertEquals(elem.getUUData().getUUIndicator().getData(), new Integer(129));
        assertTrue(elem.getCollectInformationAllowed());
        assertTrue(elem.getReleaseCallArgExtensionAllowed());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDPArgExtensionV1Impl.class);
    	
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        InitialDPArgExtension elem = new InitialDPArgExtensionV1Impl(null, gmscAddress);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.national, "111111");
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "2222", 1, 0);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberIsupImpl forwardingDestinationNumber = new CalledPartyNumberIsupImpl(calledPartyNumber);
        elem = new InitialDPArgExtensionV3Impl(gmscAddress, forwardingDestinationNumber, null, null, null, null, null, null,
                null, null, null, false, null, false, false);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        MSClassmark2Impl msClassmark2 = new MSClassmark2Impl(Unpooled.wrappedBuffer(getMSClassmark2Data()));
        IMEIImpl imei = new IMEIImpl("00000112345");
        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        // boolean phase1, boolean phase2, boolean phase3, boolean phase4
        OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl(false, false, true, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false);

        UserServiceInformation userServiceInformation = new UserServiceInformationImpl();
        userServiceInformation.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        BearerIsupImpl bearerCap = new BearerIsupImpl(userServiceInformation);
        BearerCapabilityImpl bearerCapability2 = new BearerCapabilityImpl(bearerCap);

        ExtBearerServiceCodeImpl extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extBearerService);
        UserTeleserviceInformation userTeleserviceInformation = new UserTeleserviceInformationImpl();
        userTeleserviceInformation.setCodingStandard(UserTeleserviceInformation._CODING_STANDARD_ISO_IEC);
        HighLayerCompatibilityIsupImpl highLayerCompatibility2 = new HighLayerCompatibilityIsupImpl(userTeleserviceInformation);
        LowLayerCompatibilityImpl lowLayerCompatibility = new LowLayerCompatibilityImpl(Unpooled.wrappedBuffer(getLowLayerCompatibilityData()));
        LowLayerCompatibilityImpl lowLayerCompatibility2 = new LowLayerCompatibilityImpl(Unpooled.wrappedBuffer(getLowLayerCompatibility2Data()));
        UUIndicatorImpl uuIndicator = new UUIndicatorImpl(129);
        UUDataImpl uuData = new UUDataImpl(uuIndicator, null, false, null);
        elem = new InitialDPArgExtensionV3Impl(null, null, msClassmark2, imei, supportedCamelPhases, offeredCamel4Functionalities, bearerCapability2,
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
    }
}
