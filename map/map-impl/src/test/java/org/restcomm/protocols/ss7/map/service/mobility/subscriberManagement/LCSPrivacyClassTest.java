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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClient;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
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
public class LCSPrivacyClassTest {

	public byte[] getData() {
        return new byte[] { 48, (byte) 130, 1, 56, 4, 1, 33, 4, 1, 10, (byte) 128, 1, 3, (byte) 161, 98, 48, 96, 48, 47, (byte) 128, 4, (byte) 145, 34, 34,
                (byte) 248, (byte) 161, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 0, (byte) 129, 1, 3, (byte) 162, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 162, 6, 10, 1, 0, 10, 1,
                1, (byte) 163, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, (byte) 161, 3, 31, 32, 33, (byte) 164, 98, 48, 96, 48, 47, (byte) 128, 4, (byte) 145, 34, 34, (byte) 248, (byte) 161, 39, (byte) 160,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32,
                33, (byte) 128, 1, 0, (byte) 129, 1, 3, (byte) 162, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 165, 52, 48, 50, 2, 1, 1, (byte) 128, 1, 0, (byte) 129, 1, 3,
                (byte) 162, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, (byte) 161, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSPrivacyClassImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSPrivacyClassImpl);
        LCSPrivacyClassImpl prim = (LCSPrivacyClassImpl)result.getResult();
        
        assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        ExtSSStatus ssStatus = prim.getSsStatus();
        assertFalse(ssStatus.getBitA());
        assertFalse(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(prim.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        List<ExternalClient> externalClientList = prim.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        ExternalClient externalClient = externalClientList.get(0);
        MAPExtensionContainer extensionContainerExternalClient = externalClient.getExtensionContainer();
        LCSClientExternalID clientIdentity = externalClient.getClientIdentity();
        ISDNAddressString externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        List<LCSClientInternalID> plmnClientList = prim.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        List<ExternalClient> extExternalClientList = prim.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
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

        List<ServiceType> serviceTypeList = prim.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        ServiceType serviceType = serviceTypeList.get(0);
        MAPExtensionContainer extensionContainerServiceType = serviceType.getExtensionContainer();
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerServiceType);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerServiceType));

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSPrivacyClassImpl.class);
    	
    	SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(true, false, true, false);
        NotificationToMSUser notificationToMSUser = NotificationToMSUser.locationNotAllowed;
        List<ExternalClient> externalClientList = new ArrayList<ExternalClient>();

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ISDNAddressStringImpl externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        LCSClientExternalIDImpl clientIdentity = new LCSClientExternalIDImpl(externalAddress, extensionContainer);
        GMLCRestriction gmlcRestriction = GMLCRestriction.gmlcList;
        ExternalClientImpl externalClient = new ExternalClientImpl(clientIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        externalClientList.add(externalClient);

        List<LCSClientInternalID> plmnClientList = new ArrayList<LCSClientInternalID>();
        LCSClientInternalID lcsClientInternalIdOne = LCSClientInternalID.broadcastService;
        LCSClientInternalID lcsClientInternalIdTwo = LCSClientInternalID.oandMHPLMN;
        plmnClientList.add(lcsClientInternalIdOne);
        plmnClientList.add(lcsClientInternalIdTwo);

        List<ExternalClient> extExternalClientList = new ArrayList<ExternalClient>();
        extExternalClientList.add(externalClient);

        List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
        int serviceTypeIdentity = 1;
        ServiceTypeImpl serviceType = new ServiceTypeImpl(serviceTypeIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        serviceTypeList.add(serviceType);

        LCSPrivacyClassImpl prim = new LCSPrivacyClassImpl(ssCode, ssStatus, notificationToMSUser, externalClientList,
                plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
