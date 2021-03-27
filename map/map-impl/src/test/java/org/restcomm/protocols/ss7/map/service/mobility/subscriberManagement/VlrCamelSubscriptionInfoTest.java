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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValueImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriteriumImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfoImpl;
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
public class VlrCamelSubscriptionInfoTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 3, 10, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, -128, 1, 2, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 109, 48, 58, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 0, -92, 98, 48, 96, 10, 1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2, 1, 4, 2, 1, 1, -95, 6, -126, 1, 38, -125, 1, 0, -126, 1, 0, -93, 3, 4, 1, 7, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 66, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -90, 118, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -124, 0, -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88, 21, 48, 19, 10, 1, 13, -96, 6, -126, 1, 38, -125, 1, 0, -95, 6, 4, 1, 7, 4, 1, 6, -87, 123, -96, 67, 48, 65, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 2, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -86, 118, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VlrCamelSubscriptionInfoImpl.class);
    	
    	byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VlrCamelSubscriptionInfoImpl);
        VlrCamelSubscriptionInfoImpl prim = (VlrCamelSubscriptionInfoImpl)result.getResult();
        
        // oCsi
        OCSIImpl oCsi = prim.getOCsi();
        List<OBcsmCamelTDPDataImpl> lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        OBcsmCamelTDPDataImpl cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        // extensionContainer
        MAPExtensionContainerImpl extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        // ssCsi
        SSCSIImpl ssCsi = prim.getSsCsi();
        SSCamelDataImpl ssCamelData = ssCsi.getSsCamelData();

        List<SSCodeImpl> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        SSCodeImpl one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allCommunityOfInterestSS);
        ISDNAddressStringImpl gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(!ssCsi.getNotificationToCSE());

        // oBcsmCamelTDPCriteriaList
        List<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList = prim.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        DestinationNumberCriteriaImpl destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        List<ISDNAddressStringImpl> destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        ISDNAddressStringImpl destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        ISDNAddressStringImpl destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        List<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        ExtBasicServiceCodeImpl basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);

        ExtBasicServiceCodeImpl basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        List<CauseValueImpl> oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        // TifCsi
        assertFalse(prim.getTifCsi());

        // mCsi
        MCSIImpl mCsi = prim.getMCsi();
        List<MMCodeImpl> mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCodeImpl mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        MMCodeImpl mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        ISDNAddressStringImpl gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(!mCsi.getNotificationToCSE());

        // smsCsi
        SMSCSIImpl smsCsi = prim.getSmsCsi();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPDataImpl smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(!smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        // vtCsi
        TCSIImpl vtCsi = prim.getVtCsi();
        List<TBcsmCamelTDPDataImpl> tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        TBcsmCamelTDPDataImpl tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        // tBcsmCamelTdpCriteriaList
        List<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList = prim.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        TBcsmCamelTdpCriteriaImpl tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        List<CauseValueImpl> oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        // dCsi
        DCSIImpl dCsi = prim.getDCsi();
        List<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressStringImpl dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressStringImpl gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        // mtSmsCSI
        SMSCSIImpl mtSmsCSI = prim.getMtSmsCSI();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        SMSCAMELTDPDataImpl smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(!mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        // mtSmsCamelTdpCriteriaList
        List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList = prim.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteriaImpl mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        List<MTSMSTPDUType> tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VlrCamelSubscriptionInfoImpl.class);
    	
        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tBusy;
        ArrayList<ExtBasicServiceCodeImpl> basicServiceCriteria = new ArrayList<ExtBasicServiceCodeImpl>();
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices);
        ExtBasicServiceCodeImpl basicServiceOne = new ExtBasicServiceCodeImpl(b);
        ExtBasicServiceCodeImpl basicServiceTwo = new ExtBasicServiceCodeImpl(extTeleservice);
        basicServiceCriteria.add(basicServiceOne);
        basicServiceCriteria.add(basicServiceTwo);

        ArrayList<CauseValueImpl> tCauseValueCriteria = new ArrayList<CauseValueImpl>();
        tCauseValueCriteria.add(new CauseValueImpl(7));
        tCauseValueCriteria.add(new CauseValueImpl(6));

        ISDNAddressStringImpl gsmSCFAddressOne = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "1122333");
        OBcsmCamelTDPDataImpl cind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, 3,
                gsmSCFAddressOne, DefaultCallHandling.releaseCall, null);
        ArrayList<OBcsmCamelTDPDataImpl> lst = new ArrayList<OBcsmCamelTDPDataImpl>();
        lst.add(cind);

        OCSIImpl oCsi = new OCSIImpl(lst, null, 2, false, false);
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ArrayList<SSCodeImpl> ssEventList = new ArrayList<SSCodeImpl>();
        ssEventList.add(new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS.getCode()));
        ISDNAddressStringImpl gsmSCFAddressTwo = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        SSCamelDataImpl ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddressTwo, extensionContainer);
        boolean notificationToCSE = false;
        boolean csiActive = true;

        SSCSIImpl ssCsi = new SSCSIImpl(ssCamelData, extensionContainer, notificationToCSE, csiActive);

        ArrayList<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteriaImpl>();
        OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.collectedInfo;
        ISDNAddressStringImpl destinationNumberOne = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        ISDNAddressStringImpl destinationNumberTwo = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22235");
        ArrayList<ISDNAddressStringImpl> destinationNumberList = new ArrayList<ISDNAddressStringImpl>();
        destinationNumberList.add(destinationNumberOne);
        destinationNumberList.add(destinationNumberTwo);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<Integer>();
        destinationNumberLengthList.add(2);
        destinationNumberLengthList.add(4);
        destinationNumberLengthList.add(1);
        DestinationNumberCriteriaImpl destinationNumberCriteria = new DestinationNumberCriteriaImpl(MatchType.enabling,
                destinationNumberList, destinationNumberLengthList);

        CallTypeCriteria callTypeCriteria = CallTypeCriteria.forwarded;
        ArrayList<CauseValueImpl> oCauseValueCriteria = new ArrayList<CauseValueImpl>();
        oCauseValueCriteria.add(new CauseValueImpl(7));

        OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTriggerDetectionPoint,
                destinationNumberCriteria, basicServiceCriteria, callTypeCriteria, oCauseValueCriteria, extensionContainer);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);

        boolean tifCsi = false;

        ArrayList<MMCodeImpl> mobilityTriggers = new ArrayList<MMCodeImpl>();
        Long serviceKey = 3L;
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        ;
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.GPRSAttach));
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.IMSIAttach));

        MCSIImpl mCsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extensionContainer, notificationToCSE, csiActive);

        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        ArrayList<SMSCAMELTDPDataImpl> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPDataImpl>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSMSHandling, extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        Integer camelCapabilityHandling = 8;

        SMSCSIImpl smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);

        TBcsmCamelTDPDataImpl tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized,
                3, gsmSCFAddress, DefaultCallHandling.releaseCall, null);
        ArrayList<TBcsmCamelTDPDataImpl> tBcsmCamelTDPDatalst = new ArrayList<TBcsmCamelTDPDataImpl>();
        tBcsmCamelTDPDatalst.add(tBcsmCamelTDPData);
        TCSIImpl vtCsi = new TCSIImpl(tBcsmCamelTDPDatalst, null, 2, false, false);

        TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint,
                basicServiceCriteria, tCauseValueCriteria);
        ArrayList<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteriaImpl>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22234");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        ArrayList<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriteriumImpl>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSIImpl dCsi = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

        SMSCSIImpl mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        ArrayList<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteriaImpl>();
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);

        MTsmsCAMELTDPCriteriaImpl mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint,
                tPDUTypeCriterion);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);

        VlrCamelSubscriptionInfoImpl prim = new VlrCamelSubscriptionInfoImpl(oCsi, extensionContainer, ssCsi,
                oBcsmCamelTDPCriteriaList, tifCsi, mCsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCsi, mtSmsCSI,
                mtSmsCamelTdpCriteriaList);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData();
        assertEquals(encodedData, rawData);
    }
}
