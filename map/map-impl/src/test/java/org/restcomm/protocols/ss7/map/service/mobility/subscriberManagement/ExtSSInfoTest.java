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
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscriptionImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOptionImpl;
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
public class ExtSSInfoTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -124, -96, -127, -127, 4, 1, 0, 48, 77, 48, 75, -126, 1, 38, -124, 1, 3, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1, 2, -87, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 109, -95, 107, 4, 1, 0, 48, 55, 48, 53, -126, 1, 38, -124, 1, 3, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    public byte[] getData2() {
        return new byte[] { 48, -127, -78, -94, -127, -81, 48, 66, 48, 64, 2, 1, 1, 4, 4, 1, 2, 3, 4, 10, 1, 0, 48, 3, -126, 1, 38, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, 58, 48, 56, -126, 1, 38, 2, 1, 1, 4, 1, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 63, -93, 61, 4, 1, 0, -124, 1, 3, -126, 1, 0, 48, 3, -126, 1, 38, -91, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    public byte[] getData4() {
        return new byte[] { 48, 55, -92, 53, 2, 1, 2, 2, 1, 1, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    private byte[] getISDNSubaddressStringData() {
        return new byte[] { 2, 5 };
    }

    private byte[] getcugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtSSInfoImpl.class);
    	
        // option 1
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtSSInfoImpl);
        ExtSSInfoImpl prim = (ExtSSInfoImpl)result.getResult();
        
        ExtForwInfoImpl forwardingInfo = prim.getForwardingInfo();
        ExtCallBarInfoImpl callBarringInfo = prim.getCallBarringInfo();
        CUGInfoImpl cugInfo = prim.getCugInfo();
        ExtSSDataImpl ssData = prim.getSsData();
        EMLPPInfoImpl emlppInfo = prim.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        MAPExtensionContainerImpl extensionContainer = forwardingInfo.getExtensionContainer();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        List<ExtForwFeatureImpl> forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        ExtForwFeatureImpl extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(!extForwFeature.getSsStatus().getBitP());
        assertTrue(!extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        ISDNAddressStringImpl forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(extForwFeature.getForwardedToSubaddress().getData(), this.getISDNSubaddressStringData()));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(),
                ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        FTNAddressStringImpl longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        // option 2
        data = this.getData1();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtSSInfoImpl);
        prim = (ExtSSInfoImpl)result.getResult();

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNotNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        extensionContainer = callBarringInfo.getExtensionContainer();
        assertEquals(callBarringInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        assertNotNull(callBarringInfo.getCallBarringFeatureList());
        assertEquals(callBarringInfo.getCallBarringFeatureList().size(), 1);
        assertNotNull(callBarringInfo.getCallBarringFeatureList().get(0));
        assertNotNull(extensionContainer);

        // option 3
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtSSInfoImpl);
        prim = (ExtSSInfoImpl)result.getResult();

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNotNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        extensionContainer = cugInfo.getExtensionContainer();
        assertNotNull(cugInfo.getCUGSubscriptionList());
        assertEquals(cugInfo.getCUGSubscriptionList().size(), 1);
        assertNotNull(cugInfo.getCUGSubscriptionList().get(0));
        assertNotNull(cugInfo.getCUGFeatureList());
        assertEquals(cugInfo.getCUGFeatureList().size(), 1);
        assertNotNull(cugInfo.getCUGFeatureList().get(0));
        assertNotNull(extensionContainer);

        // option 4
        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtSSInfoImpl);
        prim = (ExtSSInfoImpl)result.getResult();

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNotNull(ssData);
        assertNull(emlppInfo);

        extensionContainer = ssData.getExtensionContainer();
        assertEquals(ssData.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        assertNotNull(ssData.getSsStatus());
        assertTrue(ssData.getSsStatus().getBitA());
        assertTrue(!ssData.getSsStatus().getBitP());
        assertTrue(!ssData.getSsStatus().getBitQ());
        assertTrue(ssData.getSsStatus().getBitR());
        assertNotNull(ssData.getSSSubscriptionOption());
        assertNotNull(ssData.getSSSubscriptionOption().getCliRestrictionOption());
        assertEquals(ssData.getSSSubscriptionOption().getCliRestrictionOption(), CliRestrictionOption.permanent);
        assertNull(ssData.getSSSubscriptionOption().getOverrideCategory());
        assertNotNull(extensionContainer);

        // option 5
        data = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtSSInfoImpl);
        prim = (ExtSSInfoImpl)result.getResult();

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNotNull(emlppInfo);

        extensionContainer = emlppInfo.getExtensionContainer();
        assertEquals(emlppInfo.getMaximumentitledPriority(), 2);
        assertEquals(emlppInfo.getDefaultPriority(), 1);
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtSSInfoImpl.class);
    	        
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(false, false, true, true);
        ISDNAddressStringImpl forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNSubaddressStringImpl forwardedToSubaddress = new ISDNSubaddressStringImpl(this.getISDNSubaddressStringData());
        ExtForwOptionsImpl forwardingOptions = new ExtForwOptionsImpl(true, false, true, ExtForwOptionsForwardingReason.msBusy);
        Integer noReplyConditionTime = 2;
        FTNAddressStringImpl longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22227");

        ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber,
                forwardedToSubaddress, forwardingOptions, noReplyConditionTime, extensionContainer, longForwardedToNumber);

        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
        ArrayList<ExtForwFeatureImpl> forwardingFeatureList = new ArrayList<ExtForwFeatureImpl>();
        forwardingFeatureList.add(extForwFeature);
        ExtForwInfoImpl forwardingInfo = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);

        ExtCallBarringFeatureImpl callBarringFeature = new ExtCallBarringFeatureImpl(basicService, ssStatus, extensionContainer);
        ArrayList<ExtCallBarringFeatureImpl> callBarringFeatureList = new ArrayList<ExtCallBarringFeatureImpl>();
        callBarringFeatureList.add(callBarringFeature);

        ExtCallBarInfoImpl callBarringInfo = new ExtCallBarInfoImpl(ssCode, callBarringFeatureList, extensionContainer);

        Integer preferentialCugIndicator = 1;
        InterCUGRestrictionsImpl interCugRestrictions = new InterCUGRestrictionsImpl(0);
        CUGFeatureImpl cugFeature = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions,
                extensionContainer);
        ArrayList<CUGFeatureImpl> cugFeatureList = new ArrayList<CUGFeatureImpl>();
        cugFeatureList.add(cugFeature);

        ArrayList<CUGSubscriptionImpl> cugSubscriptionList = new ArrayList<CUGSubscriptionImpl>();
        int cugIndex = 1;
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(getcugData());
        IntraCUGOptions intraCugOptions = IntraCUGOptions.getInstance(0);
        ArrayList<ExtBasicServiceCodeImpl> basicServiceList = new ArrayList<ExtBasicServiceCodeImpl>();
        basicServiceList.add(basicService);
        CUGSubscriptionImpl cugSubscription = new CUGSubscriptionImpl(cugIndex, cugInterlock, intraCugOptions,
                basicServiceList, extensionContainer);
        cugSubscriptionList.add(cugSubscription);

        CUGInfoImpl cugInfo = new CUGInfoImpl(cugSubscriptionList, cugFeatureList, extensionContainer);

        SSSubscriptionOptionImpl ssSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.permanent);

        ArrayList<ExtBasicServiceCodeImpl> basicServiceGroupList = new ArrayList<ExtBasicServiceCodeImpl>();
        basicServiceGroupList.add(basicService);

        ExtSSDataImpl ssData = new ExtSSDataImpl(ssCode, ssStatus, ssSubscriptionOption, basicServiceGroupList, extensionContainer);

        EMLPPInfoImpl emlppInfo = new EMLPPInfoImpl(2, 1, extensionContainer);

        // option 1
        ExtSSInfoImpl prim = new ExtSSInfoImpl(forwardingInfo);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 2
        prim = new ExtSSInfoImpl(callBarringInfo);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData1();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 3
        prim = new ExtSSInfoImpl(cugInfo);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData2();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 4
        prim = new ExtSSInfoImpl(ssData);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData3();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 5
        prim = new ExtSSInfoImpl(emlppInfo);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData4();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}