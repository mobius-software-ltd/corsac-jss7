package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class RequestedSubscriptionInfoTest {
    private byte[] data = {48, 77, -95, 8, 4, 1, 64, -126, 1, 0, -124, 0, -126, 0, -125, 1, 0, -124, 0, -123, 0, -90,
            39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
            22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -121, 1, 0, -120, 0, -119, 0, -118, 0, -117, 0, -116, 0, -115, 0,
            -114, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RequestedSubscriptionInfoImpl.class);
    	                
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestedSubscriptionInfoImpl);
        RequestedSubscriptionInfoImpl requestedSubscriptionInfo = (RequestedSubscriptionInfoImpl)result.getResult();

        assertNotNull(requestedSubscriptionInfo.getRequestedSSInfo());
        assertTrue(requestedSubscriptionInfo.getOdb());
        assertEquals(requestedSubscriptionInfo.getRequestedCAMELSubscriptionInfo(), RequestedCAMELSubscriptionInfo.oCSI);
        assertTrue(requestedSubscriptionInfo.getSupportedVlrCamelPhases());
        assertTrue(requestedSubscriptionInfo.getSupportedSgsnCamelPhases());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(requestedSubscriptionInfo.getExtensionContainer()));
        assertEquals(requestedSubscriptionInfo.getAdditionalRequestedCamelSubscriptionInfo(),
                AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI);
        assertTrue(requestedSubscriptionInfo.getMsisdnBsList());
        assertTrue(requestedSubscriptionInfo.getCsgSubscriptionDataRequested());
        assertTrue(requestedSubscriptionInfo.getCwInfo());
        assertTrue(requestedSubscriptionInfo.getClipInfo());
        assertTrue(requestedSubscriptionInfo.getClirInfo());
        assertTrue(requestedSubscriptionInfo.getHoldInfo());
        assertTrue(requestedSubscriptionInfo.getEctInfo());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RequestedSubscriptionInfoImpl.class);
    	                
        BearerServiceCodeImpl bearerServiceCode = new BearerServiceCodeImpl(BearerServiceCodeValue.allBearerServices);
        BasicServiceCodeImpl basicServiceCode = new BasicServiceCodeImpl(bearerServiceCode);
        SSForBSCodeImpl ssForBSCode = new SSForBSCodeImpl(new SSCodeImpl(SupplementaryCodeValue.allCallCompletionSS), basicServiceCode, true);

        RequestedSubscriptionInfoImpl requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl(ssForBSCode, true,
                RequestedCAMELSubscriptionInfo.oCSI, true, true, MAPExtensionContainerTest.GetTestExtensionContainer(),
                AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI, true, true, true, true, true, true, true);

        ByteBuf buffer=parser.encode(requestedSubscriptionInfo);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}