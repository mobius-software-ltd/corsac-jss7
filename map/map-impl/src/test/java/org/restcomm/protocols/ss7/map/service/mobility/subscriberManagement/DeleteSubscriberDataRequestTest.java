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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
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
public class DeleteSubscriberDataRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 8, (byte) 128, 6, 17, 33, 34, 51, 67, 68 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 74, -128, 6, 17, 33, 34, 51, 67, 68, -95, 3, -126, 1, 48, -94, 6, 4, 1, 33, 4, 1, 17, -124, 0, -123, 2, 0, 11, -121, 0, -120,
                0, -119, 0, -90, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33 };
    }


    private byte[] getEncodedData3() {
        return new byte[] { 48, 41, -128, 6, 17, 33, 34, 51, 67, 68, -86, 2, 5, 0, -117, 0, -84, 2, 5, 0, -115, 0, -114, 0, -113, 2, 0, -112, -112, 0, -111, 0, -78, 5, 48, 3, 2, 1, 15, -109, 0, -108, 0 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DeleteSubscriberDataRequestImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DeleteSubscriberDataRequestImpl);
        DeleteSubscriberDataRequestImpl asc = (DeleteSubscriberDataRequestImpl)result.getResult();
        
        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertNull(asc.getBasicServiceList());
        assertNull(asc.getSsList());
        assertFalse(asc.getRoamingRestrictionDueToUnsupportedFeature());
        assertNull(asc.getRegionalSubscriptionIdentifier());
        assertFalse(asc.getVbsGroupIndication());
        assertFalse(asc.getVgcsGroupIndication());
        assertFalse(asc.getCamelSubscriptionInfoWithdraw());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getGPRSSubscriptionDataWithdraw());
        assertFalse(asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature());
        assertNull(asc.getLSAInformationWithdraw());
        assertFalse(asc.getGmlcListWithdraw());
        assertFalse(asc.getIstInformationWithdraw());
        assertNull(asc.getSpecificCSIWithdraw());
        assertFalse(asc.getChargingCharacteristicsWithdraw());
        assertFalse(asc.getStnSrWithdraw());
        assertNull(asc.getEPSSubscriptionDataWithdraw());
        assertFalse(asc.getApnOiReplacementWithdraw());
        assertFalse(asc.getCsgSubscriptionDeleted());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DeleteSubscriberDataRequestImpl);
        asc = (DeleteSubscriberDataRequestImpl)result.getResult();

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertEquals(asc.getBasicServiceList().size(), 1);
        assertEquals(asc.getBasicServiceList().get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allAlternateSpeech_DataCDA);
        assertEquals(asc.getSsList().size(), 2);
        assertEquals(asc.getSsList().get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        assertEquals(asc.getSsList().get(1).getSupplementaryCodeValue(), SupplementaryCodeValue.clip);
        assertTrue(asc.getRoamingRestrictionDueToUnsupportedFeature());
        assertEquals(asc.getRegionalSubscriptionIdentifier().getIntValue(), 11);
        assertTrue(asc.getVbsGroupIndication());
        assertTrue(asc.getVgcsGroupIndication());
        assertTrue(asc.getCamelSubscriptionInfoWithdraw());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        assertNull(asc.getGPRSSubscriptionDataWithdraw());
        assertFalse(asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature());
        assertNull(asc.getLSAInformationWithdraw());
        assertFalse(asc.getGmlcListWithdraw());
        assertFalse(asc.getIstInformationWithdraw());
        assertNull(asc.getSpecificCSIWithdraw());
        assertFalse(asc.getChargingCharacteristicsWithdraw());
        assertFalse(asc.getStnSrWithdraw());
        assertNull(asc.getEPSSubscriptionDataWithdraw());
        assertFalse(asc.getApnOiReplacementWithdraw());
        assertFalse(asc.getCsgSubscriptionDeleted());


        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DeleteSubscriberDataRequestImpl);
        asc = (DeleteSubscriberDataRequestImpl)result.getResult();

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertNull(asc.getBasicServiceList());
        assertNull(asc.getSsList());
        assertFalse(asc.getRoamingRestrictionDueToUnsupportedFeature());
        assertNull(asc.getRegionalSubscriptionIdentifier());
        assertFalse(asc.getVbsGroupIndication());
        assertFalse(asc.getVgcsGroupIndication());
        assertFalse(asc.getCamelSubscriptionInfoWithdraw());
        assertNull(asc.getExtensionContainer());

        assertTrue(asc.getGPRSSubscriptionDataWithdraw().getAllGPRSData());
        assertTrue(asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature());
        assertTrue(asc.getLSAInformationWithdraw().getAllLSAData());
        assertTrue(asc.getGmlcListWithdraw());
        assertTrue(asc.getIstInformationWithdraw());

        SpecificCSIWithdraw specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());

        assertTrue(asc.getChargingCharacteristicsWithdraw());
        assertTrue(asc.getStnSrWithdraw());
        assertEquals(asc.getEPSSubscriptionDataWithdraw().getContextIdList().size(), 1);
        assertEquals((int) asc.getEPSSubscriptionDataWithdraw().getContextIdList().get(0), 15);
        assertTrue(asc.getApnOiReplacementWithdraw());
        assertTrue(asc.getCsgSubscriptionDeleted());
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DeleteSubscriberDataRequestImpl.class);
    	
        IMSIImpl imsi = new IMSIImpl("111222333444");
        DeleteSubscriberDataRequestImpl asc = new DeleteSubscriberDataRequestImpl(imsi, null, null, false, null, false, false, false, null, null, false, null,
                false, false, null, false, false, null, false, false);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getEncodedData();
        assertEquals(encodedData, rawData);


        List<ExtBasicServiceCode> basicServiceList = new ArrayList<ExtBasicServiceCode>();
        ExtBearerServiceCodeImpl extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(extBearerService);
        basicServiceList.add(basicService);
        List<SSCode> ssList = new ArrayList<SSCode>();
        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        SSCodeImpl ssCode2 = new SSCodeImpl(SupplementaryCodeValue.clip);
        ssList.add(ssCode);
        ssList.add(ssCode2);
        ZoneCodeImpl regionalSubscriptionIdentifier = new ZoneCodeImpl(11);
        asc = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, true, regionalSubscriptionIdentifier, true, true, true,
                MAPExtensionContainerTest.GetTestExtensionContainer(), null, false, null, false, false, null, false, false, null, false, false);

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getEncodedData2();
        assertEquals(encodedData, rawData);


        GPRSSubscriptionDataWithdrawImpl gprsSubscriptionDataWithdraw = new GPRSSubscriptionDataWithdrawImpl(true);
        LSAInformationWithdrawImpl lsaInformationWithdraw = new LSAInformationWithdrawImpl(true);
        SpecificCSIWithdrawImpl specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true, false, false, false, false, false, false, false, false,
                false, false);
        ArrayList<Integer> contextIdList = new ArrayList<Integer>();
        contextIdList.add(15);
        EPSSubscriptionDataWithdrawImpl epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(contextIdList);
        asc = new DeleteSubscriberDataRequestImpl(imsi, null, null, false, null, false, false, false, null, gprsSubscriptionDataWithdraw, true,
                lsaInformationWithdraw, true, true, specificCSIWithdraw, true, true, epsSubscriptionDataWithdraw, true, true);

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getEncodedData3();
        assertEquals(encodedData, rawData);
    }
}