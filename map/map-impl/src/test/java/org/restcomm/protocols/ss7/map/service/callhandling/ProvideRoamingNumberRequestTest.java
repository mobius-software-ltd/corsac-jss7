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

package org.restcomm.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingCategory;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExtProtocolId;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.SignalInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.LACImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.LocationAreaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
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

/*
 *
 * @author cristian veliscu
 *
 */
public class ProvideRoamingNumberRequestTest {
    Logger logger = Logger.getLogger(ProvideRoamingNumberRequestTest.class);

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

    private byte[] getEncodedData() {
        return new byte[] { 48, -126, 1, 5, -128, 8, 16, 33, 2, 2, 16, -119, 34, -9, -127, 4, -111, 34, 34, -8, -126, 4, -111,
                34, 34, -9, -124, 4, 0, 3, 98, 39, -91, 50, 10, 1, 2, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33, -90, 50, 10, 1, 2, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -120, 4, -111, 34,
                34, -10, -119, 5, 19, -6, 61, 61, -22, -85, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6,
                3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -116, 1, 8, -113, 2, 6, -64,
                -82, 47, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -108, 2, 1, 14, -74, 4, -127, 2, 0, 123,
                -105, 1, 0, -103, 4, -111, 34, 34, -11 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, -127, -124, -128, 8, 16, 33, 2, 2, 16, -119, 34, -9, -127, 4, -111, 34, 34, -8, -126, 4, -111,
                34, 34, -9, -124, 4, 0, 3, 98, 39, -91, 50, 10, 1, 2, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33, -90, 50, 10, 1, 2, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, -126, 1, 23, -128, 8, 16, 33, 2, 2, 16, -119, 34, -9, -127, 4, -111, 34, 34, -8, -126, 4, -111,
                34, 34, -9, -124, 4, 0, 3, 98, 39, -91, 50, 10, 1, 2, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33, -90, 50, 10, 1, 2, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -121, 0, -120, 4,
                -111, 34, 34, -10, -119, 5, 19, -6, 61, 61, -22, -118, 0, -85, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -116, 1, 8,
                -115, 0, -113, 2, 6, -64, -82, 47, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -112, 0,
                -111, 0, -110, 0, -109, 0, -108, 2, 1, 14, -107, 0, -74, 4, -127, 2, 0, 123, -105, 1, 0, -104, 0, -103, 4,
                -111, 34, 34, -11 };
    }

    public byte[] getDataLmsi() {
        return new byte[] { 0, 3, 98, 39 };
    }

    public byte[] getCallReferenceNumber() {
        return new byte[] { 19, -6, 61, 61, -22 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideRoamingNumberRequestImpl.class);

    	byte[] data=getEncodedData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideRoamingNumberRequestImpl);
        ProvideRoamingNumberRequestImpl prn = (ProvideRoamingNumberRequestImpl)result.getResult();
        
        assertEquals(prn.getMapProtocolVersion(), 3);
        IMSI imsi = prn.getImsi();
        ISDNAddressString mscNumber = prn.getMscNumber();
        ISDNAddressString msisdn = prn.getMsisdn();
        LMSI lmsi = prn.getLmsi();
        ExternalSignalInfo gsmBearerCapability = prn.getGsmBearerCapability();
        ExternalSignalInfo networkSignalInfo = prn.getNetworkSignalInfo();
        boolean suppressionOfAnnouncement = prn.getSuppressionOfAnnouncement();
        ISDNAddressString gmscAddress = prn.getGmscAddress();
        CallReferenceNumber callReferenceNumber = prn.getCallReferenceNumber();
        boolean orInterrogation = prn.getOrInterrogation();
        MAPExtensionContainer extensionContainer = prn.getExtensionContainer();
        AlertingPattern alertingPattern = prn.getAlertingPattern();
        boolean ccbsCall = prn.getCcbsCall();
        SupportedCamelPhases supportedCamelPhasesInInterrogatingNode = prn.getSupportedCamelPhasesInInterrogatingNode();
        ExtExternalSignalInfo additionalSignalInfo = prn.getAdditionalSignalInfo();
        boolean orNotSupportedInGMSC = prn.getOrNotSupportedInGMSC();
        boolean prePagingSupported = prn.getPrePagingSupported();
        boolean longFTNSupported = prn.getLongFTNSupported();
        boolean suppressVtCsi = prn.getSuppressVtCsi();
        OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode = prn.getOfferedCamel4CSIsInInterrogatingNode();
        boolean mtRoamingRetrySupported = prn.getMtRoamingRetrySupported();
        PagingArea pagingArea = prn.getPagingArea();
        EMLPPPriority callPriority = prn.getCallPriority();
        boolean mtrfIndicator = prn.getMtrfIndicator();
        ISDNAddressString oldMSCNumber = prn.getOldMSCNumber();
        long mapProtocolVersion = prn.getMapProtocolVersion();

        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");
        assertNotNull(mscNumber);
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mscNumber.getAddress(), "22228");
        assertNotNull(msisdn);
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "22227");
        assertNotNull(lmsi);
        assertTrue(ByteBufUtil.equals(lmsi.getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
        assertNotNull(gsmBearerCapability);
        assertNotNull(networkSignalInfo);
        assertFalse(suppressionOfAnnouncement);
        assertNotNull(gmscAddress);
        assertEquals(gmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gmscAddress.getAddress(), "22226");
        assertNotNull(callReferenceNumber);
        assertTrue(ByteBufUtil.equals(callReferenceNumber.getValue(), Unpooled.wrappedBuffer(getCallReferenceNumber())));
        assertFalse(orInterrogation);
        assertNotNull(extensionContainer);
        assertNotNull(alertingPattern);
        assertFalse(ccbsCall);
        assertNotNull(supportedCamelPhasesInInterrogatingNode);
        assertNotNull(additionalSignalInfo);
        assertFalse(orNotSupportedInGMSC);
        assertFalse(prePagingSupported);
        assertFalse(longFTNSupported);
        assertFalse(suppressVtCsi);
        assertNotNull(offeredCamel4CSIsInInterrogatingNode);
        assertFalse(mtRoamingRetrySupported);
        assertNotNull(pagingArea);
        assertNotNull(callPriority);
        assertFalse(mtrfIndicator);
        assertNotNull(oldMSCNumber);
        assertEquals(oldMSCNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(oldMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oldMSCNumber.getAddress(), "22225");
        assertEquals(mapProtocolVersion, 3);

        // 2
        data=getEncodedData1();
    	result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideRoamingNumberRequestImpl);
        prn = (ProvideRoamingNumberRequestImpl)result.getResult();

        imsi = prn.getImsi();
        mscNumber = prn.getMscNumber();
        msisdn = prn.getMsisdn();
        lmsi = prn.getLmsi();
        gsmBearerCapability = prn.getGsmBearerCapability();
        networkSignalInfo = prn.getNetworkSignalInfo();
        suppressionOfAnnouncement = prn.getSuppressionOfAnnouncement();
        gmscAddress = prn.getGmscAddress();
        callReferenceNumber = prn.getCallReferenceNumber();
        orInterrogation = prn.getOrInterrogation();
        extensionContainer = prn.getExtensionContainer();
        alertingPattern = prn.getAlertingPattern();
        ccbsCall = prn.getCcbsCall();
        supportedCamelPhasesInInterrogatingNode = prn.getSupportedCamelPhasesInInterrogatingNode();
        additionalSignalInfo = prn.getAdditionalSignalInfo();
        orNotSupportedInGMSC = prn.getOrNotSupportedInGMSC();
        prePagingSupported = prn.getPrePagingSupported();
        longFTNSupported = prn.getLongFTNSupported();
        suppressVtCsi = prn.getSuppressVtCsi();
        offeredCamel4CSIsInInterrogatingNode = prn.getOfferedCamel4CSIsInInterrogatingNode();
        mtRoamingRetrySupported = prn.getMtRoamingRetrySupported();
        pagingArea = prn.getPagingArea();
        callPriority = prn.getCallPriority();
        mtrfIndicator = prn.getMtrfIndicator();
        oldMSCNumber = prn.getOldMSCNumber();
        mapProtocolVersion = prn.getMapProtocolVersion();

        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");
        assertNotNull(mscNumber);
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mscNumber.getAddress(), "22228");
        assertNotNull(msisdn);
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "22227");
        assertNotNull(lmsi);
        assertTrue(ByteBufUtil.equals(lmsi.getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
        assertNotNull(gsmBearerCapability);
        assertNotNull(networkSignalInfo);

        assertFalse(suppressionOfAnnouncement);
        assertNull(gmscAddress);
        assertNull(callReferenceNumber);
        assertFalse(orInterrogation);
        assertNull(extensionContainer);
        assertNull(alertingPattern);
        assertFalse(ccbsCall);
        assertNull(supportedCamelPhasesInInterrogatingNode);
        assertNull(additionalSignalInfo);
        assertFalse(orNotSupportedInGMSC);
        assertFalse(prePagingSupported);
        assertFalse(longFTNSupported);
        assertFalse(suppressVtCsi);
        assertNull(offeredCamel4CSIsInInterrogatingNode);
        assertFalse(mtRoamingRetrySupported);
        assertNull(pagingArea);
        assertNull(callPriority);
        assertFalse(mtrfIndicator);
        assertNull(oldMSCNumber);

        // Full
        data=getEncodedDataFull();
    	result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideRoamingNumberRequestImpl);
        prn = (ProvideRoamingNumberRequestImpl)result.getResult();
        
        imsi = prn.getImsi();
        mscNumber = prn.getMscNumber();
        msisdn = prn.getMsisdn();
        lmsi = prn.getLmsi();
        gsmBearerCapability = prn.getGsmBearerCapability();
        networkSignalInfo = prn.getNetworkSignalInfo();
        suppressionOfAnnouncement = prn.getSuppressionOfAnnouncement();
        gmscAddress = prn.getGmscAddress();
        callReferenceNumber = prn.getCallReferenceNumber();
        orInterrogation = prn.getOrInterrogation();
        extensionContainer = prn.getExtensionContainer();
        alertingPattern = prn.getAlertingPattern();
        ccbsCall = prn.getCcbsCall();
        supportedCamelPhasesInInterrogatingNode = prn.getSupportedCamelPhasesInInterrogatingNode();
        additionalSignalInfo = prn.getAdditionalSignalInfo();
        orNotSupportedInGMSC = prn.getOrNotSupportedInGMSC();
        prePagingSupported = prn.getPrePagingSupported();
        longFTNSupported = prn.getLongFTNSupported();
        suppressVtCsi = prn.getSuppressVtCsi();
        offeredCamel4CSIsInInterrogatingNode = prn.getOfferedCamel4CSIsInInterrogatingNode();
        mtRoamingRetrySupported = prn.getMtRoamingRetrySupported();
        pagingArea = prn.getPagingArea();
        callPriority = prn.getCallPriority();
        mtrfIndicator = prn.getMtrfIndicator();
        oldMSCNumber = prn.getOldMSCNumber();
        mapProtocolVersion = prn.getMapProtocolVersion();

        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");
        assertNotNull(mscNumber);
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mscNumber.getAddress(), "22228");
        assertNotNull(msisdn);
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "22227");
        assertNotNull(lmsi);
        assertTrue(ByteBufUtil.equals(lmsi.getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
        assertNotNull(gsmBearerCapability);
        assertNotNull(networkSignalInfo);
        assertTrue(suppressionOfAnnouncement);
        assertNotNull(gmscAddress);
        assertEquals(gmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gmscAddress.getAddress(), "22226");
        assertNotNull(callReferenceNumber);
        assertTrue(ByteBufUtil.equals(callReferenceNumber.getValue(), Unpooled.wrappedBuffer(getCallReferenceNumber())));
        assertTrue(orInterrogation);
        assertNotNull(extensionContainer);
        assertNotNull(alertingPattern);
        assertTrue(ccbsCall);
        assertNotNull(supportedCamelPhasesInInterrogatingNode);
        assertNotNull(additionalSignalInfo);
        assertTrue(orNotSupportedInGMSC);
        assertTrue(prePagingSupported);
        assertTrue(longFTNSupported);
        assertTrue(suppressVtCsi);
        assertNotNull(offeredCamel4CSIsInInterrogatingNode);
        assertTrue(mtRoamingRetrySupported);
        assertNotNull(pagingArea);
        assertNotNull(callPriority);
        assertTrue(mtrfIndicator);
        assertNotNull(oldMSCNumber);
        assertEquals(oldMSCNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(oldMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oldMSCNumber.getAddress(), "22225");
        assertEquals(mapProtocolVersion, 3);

    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideRoamingNumberRequestImpl.class);

        IMSIImpl imsi = new IMSIImpl("011220200198227");
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22227");
        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(getDataLmsi()));

        MAPExtensionContainer extensionContainerForExtSigInfo = MAPExtensionContainerTest.GetTestExtensionContainer();
        byte[] data_ = new byte[] { 10, 20, 30, 40 };
        SignalInfoImpl signalInfo = new SignalInfoImpl(Unpooled.wrappedBuffer(data_));
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfoImpl gsmBearerCapability = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);
        ExternalSignalInfoImpl networkSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);

        boolean suppressionOfAnnouncement = false;
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22226");
        CallReferenceNumberImpl callReferenceNumber = new CallReferenceNumberImpl(Unpooled.wrappedBuffer(getCallReferenceNumber()));
        boolean orInterrogation = false;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
        boolean ccbsCall = false;
        SupportedCamelPhasesImpl supportedCamelPhasesInInterrogatingNode = new SupportedCamelPhasesImpl(true, true, false, false);
        MAPExtensionContainer extensionContainerforAddSigInfo = MAPExtensionContainerTest.GetTestExtensionContainer();
        ExtExternalSignalInfoImpl additionalSignalInfo = new ExtExternalSignalInfoImpl(signalInfo,
                ExtProtocolId.getExtProtocolId(0), extensionContainerforAddSigInfo);
        boolean orNotSupportedInGMSC = false;
        boolean prePagingSupported = false;
        boolean longFTNSupported = false;
        boolean suppressVtCsi = false;
        OfferedCamel4CSIsImpl offeredCamel4CSIsInInterrogatingNode = new OfferedCamel4CSIsImpl(false, false, false, false,
                true, true, true);
        boolean mtRoamingRetrySupported = false;
        List<LocationArea> locationAreas = new ArrayList<LocationArea>();
        LACImpl lac = new LACImpl(123);
        LocationAreaImpl la = new LocationAreaImpl(lac);
        locationAreas.add(la);
        PagingAreaImpl pagingArea = new PagingAreaImpl(locationAreas);
        EMLPPPriority callPriority = EMLPPPriority.getEMLPPPriority(0);
        boolean mtrfIndicator = false;
        ISDNAddressStringImpl oldMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22225");
        long mapProtocolVersion = 3;

        ProvideRoamingNumberRequestImpl prn = new ProvideRoamingNumberRequestImpl(imsi, mscNumber, msisdn, lmsi,
                gsmBearerCapability, networkSignalInfo, suppressionOfAnnouncement, gmscAddress, callReferenceNumber,
                orInterrogation, extensionContainer, alertingPattern, ccbsCall, supportedCamelPhasesInInterrogatingNode,
                additionalSignalInfo, orNotSupportedInGMSC, prePagingSupported, longFTNSupported, suppressVtCsi,
                offeredCamel4CSIsInInterrogatingNode, mtRoamingRetrySupported, pagingArea, callPriority, mtrfIndicator,
                oldMSCNumber, mapProtocolVersion);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(prn);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // Full
        suppressionOfAnnouncement = true;
        orInterrogation = true;
        ccbsCall = true;
        orNotSupportedInGMSC = true;
        prePagingSupported = true;
        longFTNSupported = true;
        suppressVtCsi = true;
        mtRoamingRetrySupported = true;
        mtrfIndicator = true;
        mapProtocolVersion = 3;

        prn = new ProvideRoamingNumberRequestImpl(imsi, mscNumber, msisdn, lmsi, gsmBearerCapability, networkSignalInfo,
                suppressionOfAnnouncement, gmscAddress, callReferenceNumber, orInterrogation, extensionContainer,
                alertingPattern, ccbsCall, supportedCamelPhasesInInterrogatingNode, additionalSignalInfo, orNotSupportedInGMSC,
                prePagingSupported, longFTNSupported, suppressVtCsi, offeredCamel4CSIsInInterrogatingNode,
                mtRoamingRetrySupported, pagingArea, callPriority, mtrfIndicator, oldMSCNumber, mapProtocolVersion);

        data=getEncodedDataFull();
        buffer=parser.encode(prn);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // 2
        mapProtocolVersion = 2;
        prn = new ProvideRoamingNumberRequestImpl(imsi, mscNumber, msisdn, lmsi, gsmBearerCapability, networkSignalInfo, false,
                null, null, false, null, null, false, null, null, false, false, false, false, null, false, null, null, false,
                null, mapProtocolVersion);

        data=getEncodedData1();
        buffer=parser.encode(prn);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}