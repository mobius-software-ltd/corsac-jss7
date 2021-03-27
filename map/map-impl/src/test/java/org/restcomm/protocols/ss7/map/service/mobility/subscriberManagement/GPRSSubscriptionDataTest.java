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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribedImpl;
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
public class GPRSSubscriptionDataTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -81, -95, 115, 48, 113, 2, 1, 1, -112, 2, 5, 3, -111, 3, 5, 6, 7, -110, 3, 4, 7, 7, -108, 2, 6, 7, -75, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 2, 1, 7, -127, 2, 6, 5, -126, 2, 1, 8, -125, 2, 2, 6, -124, 1, 2, -123, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -122, 2, 6, 5, -121, 3, 4, 6, 5, -120, 1, 0, -119, 1, 2, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    }

    public byte[] getAPNOIReplacementData() {
        return new byte[] { 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    };

    public byte[] getPDPTypeData() {
        return new byte[] { 5, 3 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getPDPAddressData2() {
        return new byte[] { 4, 6, 5 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    public byte[] getExt2QoSSubscribedData() {
        return new byte[] { 1, 8 };
    };

    public byte[] getExt3QoSSubscribedData() {
        return new byte[] { 2, 6 };
    };

    public byte[] getChargingCharacteristicsData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getExtPDPTypeData() {
        return new byte[] { 6, 5 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GPRSSubscriptionDataImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSSubscriptionDataImpl);
        GPRSSubscriptionDataImpl prim = (GPRSSubscriptionDataImpl)result.getResult();
        
        assertTrue(!prim.getCompleteDataListIncluded());
        List<PDPContextImpl> gprsDataList = prim.getGPRSDataList();
        assertNotNull(gprsDataList);
        assertEquals(gprsDataList.size(), 1);
        PDPContextImpl pdpContext = gprsDataList.get(0);
        assertNotNull(pdpContext);
        APNImpl apn = pdpContext.getAPN();
        assertTrue(Arrays.equals(apn.getData(), this.getAPNData()));
        APNOIReplacementImpl apnoiReplacement = pdpContext.getAPNOIReplacement();
        assertTrue(Arrays.equals(apnoiReplacement.getData(), this.getAPNOIReplacementData()));
        ChargingCharacteristicsImpl chargingCharacteristics = pdpContext.getChargingCharacteristics();
        assertTrue(Arrays.equals(chargingCharacteristics.getData(), this.getChargingCharacteristicsData()));

        Ext2QoSSubscribedImpl ext2QoSSubscribed = pdpContext.getExt2QoSSubscribed();
        assertTrue(Arrays.equals(ext2QoSSubscribed.getData(), this.getExt2QoSSubscribedData()));
        Ext3QoSSubscribedImpl ext3QoSSubscribed = pdpContext.getExt3QoSSubscribed();
        assertTrue(Arrays.equals(ext3QoSSubscribed.getData(), this.getExt3QoSSubscribedData()));
        Ext4QoSSubscribedImpl ext4QoSSubscribed = pdpContext.getExt4QoSSubscribed();
        assertEquals(ext4QoSSubscribed.getData(), 2);
        MAPExtensionContainerImpl pdpContextExtensionContainer = pdpContext.getExtensionContainer();
        assertNotNull(pdpContextExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdpContextExtensionContainer));
        PDPAddressImpl extpdpAddress = pdpContext.getExtPDPAddress();
        assertTrue(Arrays.equals(extpdpAddress.getData(), this.getPDPAddressData2()));
        ExtPDPTypeImpl extpdpType = pdpContext.getExtPDPType();
        assertTrue(Arrays.equals(extpdpType.getData(), this.getExtPDPTypeData()));
        ExtQoSSubscribedImpl extQoSSubscribed = pdpContext.getExtQoSSubscribed();
        assertTrue(Arrays.equals(extQoSSubscribed.getData(), this.getExtQoSSubscribedData()));
        assertEquals(pdpContext.getLIPAPermission(), LIPAPermission.lipaConditional);
        PDPAddressImpl pdpAddress = pdpContext.getPDPAddress();
        assertTrue(Arrays.equals(pdpAddress.getData(), this.getPDPAddressData()));
        assertEquals(pdpContext.getPDPContextId(), 1);
        PDPTypeImpl pdpType = pdpContext.getPDPType();
        assertTrue(Arrays.equals(pdpType.getData(), this.getPDPTypeData()));
        QoSSubscribedImpl qosSubscribed = pdpContext.getQoSSubscribed();
        assertTrue(Arrays.equals(qosSubscribed.getData(), this.getQoSSubscribedData()));
        assertEquals(pdpContext.getSIPTOPermission(), SIPTOPermission.siptoAllowed);

        MAPExtensionContainerImpl extensionContainer = prim.getExtensionContainer();
        APNOIReplacementImpl apnOiReplacement = prim.getApnOiReplacement();
        assertNotNull(apnOiReplacement);
        assertTrue(Arrays.equals(apnOiReplacement.getData(), this.getAPNOIReplacementData()));
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GPRSSubscriptionDataImpl.class);
    	
        int pdpContextId = 1;
        PDPTypeImpl pdpType = new PDPTypeImpl(this.getPDPTypeData());
        PDPAddressImpl pdpAddress = new PDPAddressImpl(this.getPDPAddressData());
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        boolean vplmnAddressAllowed = false;
        APNImpl apn = new APNImpl(this.getAPNData());
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(this.getChargingCharacteristicsData());
        Ext2QoSSubscribedImpl ext2QoSSubscribed = new Ext2QoSSubscribedImpl(this.getExt2QoSSubscribedData());
        Ext3QoSSubscribedImpl ext3QoSSubscribed = new Ext3QoSSubscribedImpl(this.getExt3QoSSubscribedData());
        Ext4QoSSubscribedImpl ext4QoSSubscribed = new Ext4QoSSubscribedImpl(2);
        APNOIReplacementImpl apnoiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        ExtPDPTypeImpl extpdpType = new ExtPDPTypeImpl(this.getExtPDPTypeData());
        PDPAddressImpl extpdpAddress = new PDPAddressImpl(this.getPDPAddressData2());
        SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;

        PDPContextImpl pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn,
                extensionContainer, extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed,
                ext4QoSSubscribed, apnoiReplacement, extpdpType, extpdpAddress, sipToPermission, lipaPermission);
        ArrayList<PDPContextImpl> gprsDataList = new ArrayList<PDPContextImpl>();
        gprsDataList.add(pdpContext);

        APNOIReplacementImpl apnOiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        GPRSSubscriptionDataImpl prim = new GPRSSubscriptionDataImpl(false, gprsDataList, extensionContainer, apnOiReplacement);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
