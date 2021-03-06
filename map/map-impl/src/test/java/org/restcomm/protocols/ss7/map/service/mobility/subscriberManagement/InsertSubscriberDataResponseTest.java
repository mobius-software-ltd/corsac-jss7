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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class InsertSubscriberDataResponseTest {

    public byte[] getData() {
        return new byte[] { 48, 87, -95, 3, 4, 1, 16, -94, 3, 4, 1, 38, -93, 3, 4, 1, 0, -124, 5, 4, 74, -43, 85, 80, -123, 1, 1, -122, 2, 4, -16, -89, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -120, 2, 1, -2, -119, 5, 6, 85, 85, 85, 64 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 25, -95, 3, 4, 1, 16, -94, 3, 4, 1, 38, -93, 3, 4, 1, 0, -124, 5, 4, 74, -43, 85, 80, -123, 1, 1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InsertSubscriberDataResponseImpl.class);
    	
    	// ISD Response V3 Test
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InsertSubscriberDataResponseImpl);
        InsertSubscriberDataResponseImpl prim = (InsertSubscriberDataResponseImpl)result.getResult();
        
        // teleserviceList
        List<ExtTeleserviceCodeImpl> teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        ExtTeleserviceCodeImpl extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // bearerServiceList
        List<ExtBearerServiceCodeImpl> bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        ExtBearerServiceCodeImpl extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // ssList
        List<SSCodeImpl> ssList = prim.getSSList();
        assertNotNull(ssList);
        assertEquals(ssList.size(), 1);
        SSCodeImpl ssCode = ssList.get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        ODBGeneralDataImpl odbGeneralData = prim.getODBGeneralData();
        assertTrue(!odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertTrue(!odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertTrue(!odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertTrue(!odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!odbGeneralData.getMultipleECTBarred());
        assertTrue(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(odbGeneralData.getAllICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!odbGeneralData.getRegistrationAllCFBarred());
        assertTrue(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInternationalCFBarred());

        assertEquals(prim.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);

        SupportedCamelPhasesImpl supportedCamelPhases = prim.getSupportedCamelPhases();
        assertTrue(supportedCamelPhases.getPhase1Supported());
        assertTrue(supportedCamelPhases.getPhase2Supported());
        assertTrue(supportedCamelPhases.getPhase3Supported());
        assertTrue(supportedCamelPhases.getPhase4Supported());

        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        OfferedCamel4CSIsImpl offeredCamel4CSIs = prim.getOfferedCamel4CSIs();
        assertTrue(offeredCamel4CSIs.getDCsi());
        assertTrue(offeredCamel4CSIs.getMgCsi());
        assertTrue(offeredCamel4CSIs.getMtSmsCsi());
        assertTrue(offeredCamel4CSIs.getOCsi());
        assertTrue(offeredCamel4CSIs.getPsiEnhancements());
        assertTrue(offeredCamel4CSIs.getTCsi());
        assertTrue(offeredCamel4CSIs.getVtCsi());

        SupportedFeaturesImpl supportedFeatures = prim.getSupportedFeatures();
        assertTrue(!supportedFeatures.getOdbAllApn());
        assertTrue(supportedFeatures.getOdbHPLMNApn());
        assertTrue(!supportedFeatures.getOdbVPLMNApn());
        assertTrue(supportedFeatures.getOdbAllOg());
        assertTrue(!supportedFeatures.getOdbAllInternationalOg());
        assertTrue(supportedFeatures.getOdbAllIntOgNotToHPLMNCountry());
        assertTrue(!supportedFeatures.getOdbAllInterzonalOg());
        assertTrue(supportedFeatures.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertTrue(!supportedFeatures.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(supportedFeatures.getRegSub());
        assertTrue(!supportedFeatures.getTrace());
        assertTrue(supportedFeatures.getLcsAllPrivExcep());
        assertTrue(!supportedFeatures.getLcsUniversal());
        assertTrue(supportedFeatures.getLcsCallSessionRelated());
        assertTrue(!supportedFeatures.getLcsCallSessionUnrelated());
        assertTrue(supportedFeatures.getLcsPLMNOperator());
        assertTrue(!supportedFeatures.getLcsServiceType());
        assertTrue(supportedFeatures.getLcsAllMOLRSS());
        assertTrue(!supportedFeatures.getLcsBasicSelfLocation());
        assertTrue(supportedFeatures.getLcsAutonomousSelfLocation());
        assertTrue(!supportedFeatures.getLcsTransferToThirdParty());
        assertTrue(supportedFeatures.getSmMoPp());
        assertTrue(!supportedFeatures.getBarringOutgoingCalls());
        assertTrue(supportedFeatures.getBaoc());
        assertTrue(!supportedFeatures.getBoic());
        assertTrue(supportedFeatures.getBoicExHC());

        // IST Response V2 Test
        data = this.getData();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InsertSubscriberDataResponseImpl);
        prim = (InsertSubscriberDataResponseImpl)result.getResult();
        
        // teleserviceList
        teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // bearerServiceList
        bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // ssList
        ssList = prim.getSSList();
        assertNotNull(ssList);
        assertEquals(ssList.size(), 1);
        ssCode = ssList.get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        odbGeneralData = prim.getODBGeneralData();
        assertTrue(!odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertTrue(!odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertTrue(!odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertTrue(!odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!odbGeneralData.getMultipleECTBarred());
        assertTrue(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(odbGeneralData.getAllICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!odbGeneralData.getRegistrationAllCFBarred());
        assertTrue(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInternationalCFBarred());

        assertEquals(prim.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InsertSubscriberDataResponseImpl.class);
    	
    	// Start ISD Response Vesrion 3 Test

        // teleserviceList
        ArrayList<ExtTeleserviceCodeImpl> teleserviceList = new ArrayList<ExtTeleserviceCodeImpl>();
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);

        // bearerServiceList
        ArrayList<ExtBearerServiceCodeImpl> bearerServiceList = new ArrayList<ExtBearerServiceCodeImpl>();
        ExtBearerServiceCodeImpl extBearerServiceCode = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);

        // ssList
        ArrayList<SSCodeImpl> ssList = new ArrayList<SSCodeImpl>();
        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
        ssList.add(ssCode);

        ODBGeneralDataImpl odbGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false);

        RegionalSubscriptionResponse regionalSubscriptionResponse = RegionalSubscriptionResponse.tooManyZoneCodes;

        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, true);

        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        OfferedCamel4CSIsImpl offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);

        SupportedFeaturesImpl supportedFeatures = new SupportedFeaturesImpl(false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true);

        InsertSubscriberDataResponseImpl prim = new InsertSubscriberDataResponseImpl(3, teleserviceList, bearerServiceList,
                ssList, odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer,
                offeredCamel4CSIs, supportedFeatures);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData();
        assertEquals(encodedData, rawData);

        // Start ISD Response Vesrion 2 Test
        prim = new InsertSubscriberDataResponseImpl(2, teleserviceList, bearerServiceList, ssList, odbGeneralData,
                regionalSubscriptionResponse);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData1();
        assertEquals(encodedData, rawData);
    }
}