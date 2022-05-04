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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscription;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class ExtSSInfoTest {

	public byte[] getData() {
        return new byte[] { 48, 119, -96, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 38, -124, 1, 3, -123, 4, -111, 34, 34, -8, -120, 2, 2,
                5, -122, 1, -92, -121, 1, 2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 97, -95, 95, 4, 1, 0, 48, 49, 48, 47, -126, 1, 38, -124, 1, 3, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData2() {
        return new byte[] { 48, -127, -96, -94, -127, -99, 48, 60, 48, 58, 2, 1, 1, 4, 4, 1, 2, 3, 4, 10, 1, 0, 48, 3, -126, 1, 38, -96, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, 48, 52, 48, 50, -126, 1, 38, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 57, -93, 55, 4, 1, 0, -124, 1, 3, -126, 1, 0, 48, 3, -126, 1, 38, -91, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33 };
    };

    public byte[] getData4() {
        return new byte[] { 48, 49, -92, 47, 2, 1, 2, 2, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6,
                3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
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
        
        ExtForwInfo forwardingInfo = prim.getForwardingInfo();
        ExtCallBarInfo callBarringInfo = prim.getCallBarringInfo();
        CUGInfo cugInfo = prim.getCugInfo();
        ExtSSData ssData = prim.getSsData();
        EMLPPInfo emlppInfo = prim.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        MAPExtensionContainer extensionContainer = forwardingInfo.getExtensionContainer();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        List<ExtForwFeature> forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        ExtForwFeature extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(!extForwFeature.getSsStatus().getBitP());
        assertTrue(!extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        ISDNAddressString forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(ByteBufUtil.equals(extForwFeature.getForwardedToSubaddress().getValue(),Unpooled.wrappedBuffer(this.getISDNSubaddressStringData())));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(),
                ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        FTNAddressString longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
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
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(false, false, true, true);
        ISDNAddressStringImpl forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNSubaddressStringImpl forwardedToSubaddress = new ISDNSubaddressStringImpl(Unpooled.wrappedBuffer(this.getISDNSubaddressStringData()));
        ExtForwOptionsImpl forwardingOptions = new ExtForwOptionsImpl(true, false, true, ExtForwOptionsForwardingReason.msBusy);
        Integer noReplyConditionTime = 2;
        FTNAddressStringImpl longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22227");

        ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber,
                forwardedToSubaddress, forwardingOptions, noReplyConditionTime, extensionContainer, longForwardedToNumber);

        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
        List<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
        forwardingFeatureList.add(extForwFeature);
        ExtForwInfo forwardingInfo = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);

        ExtCallBarringFeatureImpl callBarringFeature = new ExtCallBarringFeatureImpl(basicService, ssStatus, extensionContainer);
        List<ExtCallBarringFeature> callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        callBarringFeatureList.add(callBarringFeature);

        ExtCallBarInfo callBarringInfo = new ExtCallBarInfoImpl(ssCode, callBarringFeatureList, extensionContainer);

        Integer preferentialCugIndicator = 1;
        InterCUGRestrictionsImpl interCugRestrictions = new InterCUGRestrictionsImpl(0);
        CUGFeatureImpl cugFeature = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions,
                extensionContainer);
        List<CUGFeature> cugFeatureList = new ArrayList<CUGFeature>();
        cugFeatureList.add(cugFeature);

        List<CUGSubscription> cugSubscriptionList = new ArrayList<CUGSubscription>();
        int cugIndex = 1;
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(Unpooled.wrappedBuffer(getcugData()));
        IntraCUGOptions intraCugOptions = IntraCUGOptions.getInstance(0);
        List<ExtBasicServiceCode> basicServiceList = new ArrayList<ExtBasicServiceCode>();
        basicServiceList.add(basicService);
        CUGSubscriptionImpl cugSubscription = new CUGSubscriptionImpl(cugIndex, cugInterlock, intraCugOptions,
                basicServiceList, extensionContainer);
        cugSubscriptionList.add(cugSubscription);

        CUGInfoImpl cugInfo = new CUGInfoImpl(cugSubscriptionList, cugFeatureList, extensionContainer);

        SSSubscriptionOptionImpl ssSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.permanent);

        List<ExtBasicServiceCode> basicServiceGroupList = new ArrayList<ExtBasicServiceCode>();
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