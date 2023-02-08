package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 * @author yulianoifa
 */
public class AnyTimeSubscriptionInterrogationRequestTest {
    private byte[] data = {48, 91, -96, 9, -127, 7, -111, -105, 2, 33, 67, 101, -9, -95, 26, -95, 6, 4, 1, 112, -125, 1,
            0, -126, 0, -125, 1, 0, -124, 0, -121, 1, 2, -120, 0, -118, 0, -116, 0, -114, 0, -126, 7, -111, -105, 2, 103,
            69, 35, -15, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6,
            3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -124, 0};

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AnyTimeSubscriptionInterrogationRequestImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AnyTimeSubscriptionInterrogationRequestImpl);
        AnyTimeSubscriptionInterrogationRequestImpl request = (AnyTimeSubscriptionInterrogationRequestImpl)result.getResult();
        
        assertNotNull(request.getSubscriberIdentity());
        assertNotNull(request.getGsmScfAddress());
        assertNotNull(request.getRequestedSubscriptionInfo());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(request.getExtensionContainer()));
        assertTrue(request.getLongFTNSupported());
        assertEquals(request.getGsmScfAddress().getAddress(), "79207654321");

        ISDNAddressString subscriberMsisdn = request.getSubscriberIdentity().getMSISDN();
        assertEquals(subscriberMsisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(subscriberMsisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(subscriberMsisdn.getAddress(), "79201234567");

        RequestedSubscriptionInfo subscriptionInfo = request.getRequestedSubscriptionInfo();
        assertEquals(subscriptionInfo.getRequestedSSInfo().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allChargingSS);
        assertEquals(subscriptionInfo.getRequestedSSInfo().getBasicService().getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        assertFalse(subscriptionInfo.getRequestedSSInfo().getLongFtnSupported());
        assertTrue(subscriptionInfo.getOdb());
        assertEquals(subscriptionInfo.getRequestedCAMELSubscriptionInfo(), RequestedCAMELSubscriptionInfo.oCSI);
        assertTrue(subscriptionInfo.getSupportedVlrCamelPhases());
        assertFalse(subscriptionInfo.getSupportedSgsnCamelPhases());
        assertEquals(subscriptionInfo.getAdditionalRequestedCamelSubscriptionInfo(), AdditionalRequestedCAMELSubscriptionInfo.oImCSI);
        assertTrue(subscriptionInfo.getMsisdnBsList());
        assertFalse(subscriptionInfo.getCsgSubscriptionDataRequested());
        assertTrue(subscriptionInfo.getCwInfo());
        assertFalse(subscriptionInfo.getClipInfo());
        assertTrue(subscriptionInfo.getClirInfo());
        assertFalse(subscriptionInfo.getHoldInfo());
        assertTrue(subscriptionInfo.getEctInfo());
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AnyTimeSubscriptionInterrogationRequestImpl.class);
    	
        ISDNAddressStringImpl subscriberMsisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79201234567");
        SubscriberIdentityImpl subscriberIdentity = new SubscriberIdentityImpl(subscriberMsisdn);

        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.allChargingSS);
        BasicServiceCodeImpl basicServiceCode = new BasicServiceCodeImpl(new TeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices));
        SSForBSCodeImpl ssForBSCode = new SSForBSCodeImpl(ssCode, basicServiceCode, false);
        RequestedSubscriptionInfoImpl requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl(ssForBSCode, true, RequestedCAMELSubscriptionInfo.oCSI,
                true, false, null, AdditionalRequestedCAMELSubscriptionInfo.oImCSI, true, false, true,
                false, true, false, true);

        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79207654321");

        AnyTimeSubscriptionInterrogationRequestImpl request = new AnyTimeSubscriptionInterrogationRequestImpl(subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, MAPExtensionContainerTest.GetTestExtensionContainer(), true);

        ByteBuf buffer=parser.encode(request);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
