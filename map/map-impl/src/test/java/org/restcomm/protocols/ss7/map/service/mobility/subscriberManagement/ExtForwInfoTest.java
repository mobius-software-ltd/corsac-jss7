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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ExtForwInfoTest {

	public byte[] getData() {
        return new byte[] { 48, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 38, -124, 1, 3, -123, 4, -111, 34, 34, -8, -120, 2, 2,
                5, -122, 1, -92, -121, 1, 2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 24, 4, 1, 43, 48, 19, 48, 17, (byte) 131, 1, 16, (byte) 132, 1, 15, (byte) 133, 6,
                (byte) 145, (byte) 136, 120, 119, (byte) 153, (byte) 249, (byte) 134, 1, 0 };
    };

    private byte[] getISDNSubaddressStringData() {
        return new byte[] { 2, 5 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtForwInfoImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtForwInfoImpl);
        ExtForwInfoImpl prim = (ExtForwInfoImpl)result.getResult();
        
        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        List<ExtForwFeature> forwardingFeatureList = prim.getForwardingFeatureList();
        assertNotNull(forwardingFeatureList);
        assertTrue(forwardingFeatureList.size() == 1);
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

        assertTrue(ByteBufUtil.equals(extForwFeature.getForwardedToSubaddress().getValue(), Unpooled.wrappedBuffer(this.getISDNSubaddressStringData())));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertTrue(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason().getCode() == ExtForwOptionsForwardingReason.msBusy
                .getCode());
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertTrue(extForwFeature.getNoReplyConditionTime().equals(2));
        FTNAddressString longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtForwInfoImpl);
        prim = (ExtForwInfoImpl)result.getResult();
        
        extensionContainer = prim.getExtensionContainer();
        assertEquals(prim.getSsCode().getData(), Integer.valueOf(43));

        forwardingFeatureList = prim.getForwardingFeatureList();
        assertNotNull(forwardingFeatureList);
        assertTrue(forwardingFeatureList.size() == 1);
        extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allSpeechTransmissionServices);
        assertNull(extForwFeature.getBasicService().getExtBearerService());

        assertNull(extensionContainer);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtForwInfoImpl.class);
    	
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
        ExtForwInfo prim = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));

        basicService = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(
                TeleserviceCodeValue.allSpeechTransmissionServices));
        ssStatus = new ExtSSStatusImpl(true, true, true, true);
        forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "888777999");
        forwardingOptions = new ExtForwOptionsImpl(false, false, false, ExtForwOptionsForwardingReason.msNotReachable);
        extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber, null, forwardingOptions, null, null,
                null);
        ssCode = new SSCodeImpl(43);
        forwardingFeatureList = new ArrayList<ExtForwFeature>();
        forwardingFeatureList.add(extForwFeature);
        prim = new ExtForwInfoImpl(ssCode, forwardingFeatureList, null);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData2();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}