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
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClientImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSPrivacyClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MOLRClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceTypeImpl;
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
public class LCSInformationTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 7, 41, -96, 6, 4, 4, -111, 34, 50, -11, -95, -126, 5, -128, 48, -126, 1, 92, 4, 1, 33, 4, 1, 10, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, -126, 1, 92, 4, 1, 96, 4, 1, 10, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, -126, 1, 92, 4, 1, 32, 4, 1, 10, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, -126, 1, 92, 4, 1, 16, 4, 1, 10, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 55, 48, 53, 4, 1, 33, 4, 1, 10, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -93, -126, 1, 96, 48, -126, 1, 92, 4, 1, 33, 4, 1, 10, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSInformationImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSInformationImpl);
        LCSInformationImpl prim = (LCSInformationImpl)result.getResult();
        
        // start gmlcList
        List<ISDNAddressStringImpl> gmlcList = prim.getGmlcList();
        assertNotNull(gmlcList);
        assertEquals(gmlcList.size(), 1);
        ISDNAddressStringImpl isdnAddressString = gmlcList.get(0);
        assertTrue(isdnAddressString.getAddress().equals("22235"));
        assertEquals(isdnAddressString.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAddressString.getNumberingPlan(), NumberingPlan.ISDN);
        // end gmlcList

        // start lcsPrivacyExceptionList
        List<LCSPrivacyClassImpl> lcsPrivacyExceptionList = prim.getLcsPrivacyExceptionList();
        assertNotNull(lcsPrivacyExceptionList);
        assertEquals(lcsPrivacyExceptionList.size(), 4);
        LCSPrivacyClassImpl lcsPrivacyClass = lcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        ExtSSStatusImpl ssStatus = lcsPrivacyClass.getSsStatus();
        assertFalse(ssStatus.getBitA());
        assertFalse(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        List<ExternalClientImpl> externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        ExternalClientImpl externalClient = externalClientList.get(0);
        MAPExtensionContainerImpl extensionContainerExternalClient = externalClient.getExtensionContainer();
        LCSClientExternalIDImpl clientIdentity = externalClient.getClientIdentity();
        ISDNAddressStringImpl externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        List<LCSClientInternalID> plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        List<ExternalClientImpl> extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        List<ServiceTypeImpl> serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        ServiceTypeImpl serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        // end lcsPrivacyExceptionList

        // start molrlist
        List<MOLRClassImpl> molrList = prim.getMOLRList();
        assertNotNull(molrList);
        assertEquals(molrList.size(), 1);
        MOLRClassImpl molrClass = molrList.get(0);
        assertEquals(molrClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        assertFalse(molrClass.getSsStatus().getBitA());
        assertFalse(molrClass.getSsStatus().getBitP());
        assertTrue(molrClass.getSsStatus().getBitQ());
        assertTrue(molrClass.getSsStatus().getBitR());
        assertNotNull(molrClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(molrClass.getExtensionContainer()));
        // end testing molr list

        // start addLcsPrivacyExceptionList
        List<LCSPrivacyClassImpl> addLcsPrivacyExceptionList = prim.getAddLcsPrivacyExceptionList();
        assertNotNull(addLcsPrivacyExceptionList);
        assertEquals(addLcsPrivacyExceptionList.size(), 1);
        lcsPrivacyClass = addLcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        ssStatus = lcsPrivacyClass.getSsStatus();
        assertFalse(ssStatus.getBitA());
        assertFalse(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        externalClient = externalClientList.get(0);
        extensionContainerExternalClient = externalClient.getExtensionContainer();
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        // end addLcsPrivacyExceptionList
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSInformationImpl.class);
    	
        ArrayList<ISDNAddressStringImpl> gmlcList = new ArrayList<ISDNAddressStringImpl>();
        ISDNAddressStringImpl isdnAddressString = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        gmlcList.add(isdnAddressString);

        ArrayList<LCSPrivacyClassImpl> lcsPrivacyExceptionList = new ArrayList<LCSPrivacyClassImpl>();
        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(true, false, true, false);
        NotificationToMSUser notificationToMSUser = NotificationToMSUser.locationNotAllowed;
        ArrayList<ExternalClientImpl> externalClientList = new ArrayList<ExternalClientImpl>();

        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ISDNAddressStringImpl externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        LCSClientExternalIDImpl clientIdentity = new LCSClientExternalIDImpl(externalAddress, extensionContainer);
        GMLCRestriction gmlcRestriction = GMLCRestriction.gmlcList;
        ExternalClientImpl externalClient = new ExternalClientImpl(clientIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        externalClientList.add(externalClient);

        ArrayList<LCSClientInternalID> plmnClientList = new ArrayList<LCSClientInternalID>();
        LCSClientInternalID lcsClientInternalIdOne = LCSClientInternalID.broadcastService;
        LCSClientInternalID lcsClientInternalIdTwo = LCSClientInternalID.oandMHPLMN;
        plmnClientList.add(lcsClientInternalIdOne);
        plmnClientList.add(lcsClientInternalIdTwo);

        ArrayList<ExternalClientImpl> extExternalClientList = new ArrayList<ExternalClientImpl>();
        extExternalClientList.add(externalClient);

        ArrayList<ServiceTypeImpl> serviceTypeList = new ArrayList<ServiceTypeImpl>();
        int serviceTypeIdentity = 1;
        ServiceTypeImpl serviceType = new ServiceTypeImpl(serviceTypeIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        serviceTypeList.add(serviceType);

        SSCodeImpl ssCodeTwo = new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS);
        SSCodeImpl ssCodeThree = new SSCodeImpl(SupplementaryCodeValue.allForwardingSS);
        SSCodeImpl ssCodeFour = new SSCodeImpl(SupplementaryCodeValue.allLineIdentificationSS);
        LCSPrivacyClassImpl lcsPrivacyClassOne = new LCSPrivacyClassImpl(ssCode, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassTwo = new LCSPrivacyClassImpl(ssCodeTwo, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassThree = new LCSPrivacyClassImpl(ssCodeThree, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassFour = new LCSPrivacyClassImpl(ssCodeFour, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);

        lcsPrivacyExceptionList.add(lcsPrivacyClassOne);
        lcsPrivacyExceptionList.add(lcsPrivacyClassTwo);
        lcsPrivacyExceptionList.add(lcsPrivacyClassThree);
        lcsPrivacyExceptionList.add(lcsPrivacyClassFour);

        ArrayList<MOLRClassImpl> molrList = new ArrayList<MOLRClassImpl>();
        MOLRClassImpl molrClass = new MOLRClassImpl(ssCode, ssStatus, extensionContainer);
        molrList.add(molrClass);

        ArrayList<LCSPrivacyClassImpl> addLcsPrivacyExceptionList = new ArrayList<LCSPrivacyClassImpl>();
        addLcsPrivacyExceptionList.add(lcsPrivacyClassOne);

        LCSInformationImpl prim = new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList,
                addLcsPrivacyExceptionList);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
