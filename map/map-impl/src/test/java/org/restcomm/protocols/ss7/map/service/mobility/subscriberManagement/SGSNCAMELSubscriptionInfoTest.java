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

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
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
public class SGSNCAMELSubscriptionInfoTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 1, -18, -96, 120, -96, 64, 48, 62, -128, 1, 2, -127, 1, 2, -126, 4, -111, 34, 34, -8, -125, 1, 1, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 4, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -95, 120, -96, 64, 48, 62, -128, 1, 1, -127, 1, 2, -126, 4, -111, 34, 34, -8, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 4, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -93, 116, -96, 64, 48, 62, -128, 1, 1, -127, 1, 2, -126, 4, -111, 34, 34, -8, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 4, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -91, 68, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -126, 0, -125, 0 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SGSNCAMELSubscriptionInfoImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SGSNCAMELSubscriptionInfoImpl);
        SGSNCAMELSubscriptionInfoImpl prim = (SGSNCAMELSubscriptionInfoImpl)result.getResult();
        
        // start gprsCsi
        GPRSCSIImpl gprsCsi = prim.getGprsCsi();
        assertNotNull(gprsCsi.getGPRSCamelTDPDataList());
        assertEquals(gprsCsi.getGPRSCamelTDPDataList().size(), 1);
        GPRSCamelTDPDataImpl gprsCamelTDPData = gprsCsi.getGPRSCamelTDPDataList().get(0);

        MAPExtensionContainerImpl extensionContainergprsCamelTDPData = gprsCamelTDPData.getExtensionContainer();
        ISDNAddressStringImpl gsmSCFAddress = gprsCamelTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22228"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);

        assertEquals(gprsCamelTDPData.getDefaultSessionHandling(), DefaultGPRSHandling.releaseTransaction);
        assertEquals(gprsCamelTDPData.getGPRSTriggerDetectionPoint(), GPRSTriggerDetectionPoint.attachChangeOfPosition);
        assertEquals(gprsCamelTDPData.getServiceKey(), 2);

        assertNotNull(extensionContainergprsCamelTDPData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainergprsCamelTDPData));

        assertEquals(gprsCsi.getCamelCapabilityHandling().intValue(), 4);
        assertNotNull(gprsCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsCsi.getExtensionContainer()));
        assertTrue(gprsCsi.getCsiActive());
        assertTrue(gprsCsi.getNotificationToCSE());
        // end gprsCsi

        // start moSmsCsi
        SMSCSIImpl moSmsCsi = prim.getMoSmsCsi();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataList = moSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPDataImpl one = smsCamelTdpDataList.get(0);
        assertNotNull(one);
        assertEquals(one.getServiceKey(), 2);
        assertEquals(one.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressMoSmsCsi = one.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMoSmsCsi.getAddress().equals("22228"));
        assertEquals(gsmSCFAddressMoSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMoSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(one.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(one.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(one.getExtensionContainer()));

        assertNotNull(moSmsCsi.getExtensionContainer());
        assertTrue(moSmsCsi.getCsiActive());
        assertTrue(moSmsCsi.getNotificationToCSE());
        assertEquals(moSmsCsi.getCamelCapabilityHandling().intValue(), 4);
        // end moSmsCsi

        // start mtSmsCsi
        SMSCSIImpl mtSmsCsi = prim.getMtSmsCsi();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataListMtSmsCsi = mtSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListMtSmsCsi);
        assertEquals(smsCamelTdpDataListMtSmsCsi.size(), 1);
        SMSCAMELTDPDataImpl oneSmsCamelTdpDataListMtSmsCsi = smsCamelTdpDataListMtSmsCsi.get(0);
        assertNotNull(oneSmsCamelTdpDataListMtSmsCsi);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getServiceKey(), 2);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressmtSmsCsi = one.getGsmSCFAddress();
        assertTrue(gsmSCFAddressmtSmsCsi.getAddress().equals("22228"));
        assertEquals(gsmSCFAddressmtSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressmtSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(one.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(one.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(one.getExtensionContainer()));

        assertNotNull(mtSmsCsi.getExtensionContainer());
        assertFalse(mtSmsCsi.getCsiActive());
        assertFalse(mtSmsCsi.getNotificationToCSE());
        assertEquals(mtSmsCsi.getCamelCapabilityHandling().intValue(), 4);
        // end mtSmsCsi

        // start mtSmsCamelTdpCriteriaList
        List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList = prim.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteriaImpl mtSmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        List<MTSMSTPDUType> tPDUTypeCriterion = mtSmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType oneMTSMSTPDUType = tPDUTypeCriterion.get(0);
        assertNotNull(oneMTSMSTPDUType);
        assertEquals(oneMTSMSTPDUType, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType twoMTSMSTPDUType = tPDUTypeCriterion.get(1);
        assertNotNull(twoMTSMSTPDUType);
        assertEquals(twoMTSMSTPDUType, MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtSmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end mtSmsCamelTdpCriteriaList

        // start mgCsi
        MGCSIImpl mgCsi = prim.getMgCsi();
        List<MMCodeImpl> mobilityTriggers = mgCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCodeImpl oneMMCode = mobilityTriggers.get(0);
        assertNotNull(oneMMCode);
        assertEquals(oneMMCode.getMMCodeValue(), MMCodeValue.GPRSAttach);
        MMCodeImpl twoMMCode = mobilityTriggers.get(1);
        assertNotNull(twoMMCode);
        assertEquals(twoMMCode.getMMCodeValue(), MMCodeValue.IMSIAttach);

        assertEquals(mgCsi.getServiceKey(), 3);

        ISDNAddressStringImpl gsmSCFAddressMgCsi = mgCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMgCsi.getAddress().equals("22228"));
        assertEquals(gsmSCFAddressMgCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMgCsi.getNumberingPlan(), NumberingPlan.ISDN);

        assertNotNull(mgCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mgCsi.getExtensionContainer()));
        assertTrue(mgCsi.getCsiActive());
        assertTrue(mgCsi.getNotificationToCSE());
        // end mgCsi

        MAPExtensionContainerImpl extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SGSNCAMELSubscriptionInfoImpl.class);
    	
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        // start gprsCsi
        GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attachChangeOfPosition;
        long serviceKey = 2;
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.releaseTransaction;

        GPRSCamelTDPDataImpl gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSessionHandling, extensionContainer);

        ArrayList<GPRSCamelTDPDataImpl> gprsCamelTDPDataList = new ArrayList<GPRSCamelTDPDataImpl>();
        gprsCamelTDPDataList.add(gprsCamelTDPData);
        Integer camelCapabilityHandling = 4;
        boolean notificationToCSE = true;
        boolean csiActive = true;

        GPRSCSIImpl gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        // end gprsCsi

        // start moSmsCsi
        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        ArrayList<SMSCAMELTDPDataImpl> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPDataImpl>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSMSHandling, extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        SMSCSIImpl moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        // end moSmsCsi

        // start mtSmsCsi
        SMSCSIImpl mtSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, false, false);
        // end mtSmsCsi

        // start mtSmsCamelTdpCriteriaList
        ArrayList<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteriaImpl>();
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);
        MTsmsCAMELTDPCriteriaImpl mtSmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint, tPDUTypeCriterion);
        mtSmsCamelTdpCriteriaList.add(mtSmsCAMELTDPCriteria);
        // end mtSmsCamelTdpCriteriaList

        // start mgCsi
        ArrayList<MMCodeImpl> mobilityTriggers = new ArrayList<MMCodeImpl>();
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.GPRSAttach));
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.IMSIAttach));

        MGCSIImpl mgCsi = new MGCSIImpl(mobilityTriggers, 3, gsmSCFAddress, extensionContainer, true, true);
        // end mgCsi

        SGSNCAMELSubscriptionInfoImpl prim = new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi, extensionContainer, mtSmsCsi,
                mtSmsCamelTdpCriteriaList, mgCsi);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData();
        assertEquals(encodedData, rawData);
    }
}
