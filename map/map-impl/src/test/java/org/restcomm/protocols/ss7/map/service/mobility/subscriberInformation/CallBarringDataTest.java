package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.PasswordImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author vadim subbotin
 *
 */
public class CallBarringDataTest {
    private byte[] data = {48, 68, 48, 8, 48, 6, -126, 1, 0, -124, 1, 8, 18, 4, 48, 48, 48, 48, 2, 1, 3, 5, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallBarringDataImpl);
        CallBarringDataImpl callBarringData = (CallBarringDataImpl)result.getResult();

        assertEquals(callBarringData.getWrongPasswordAttemptsCounter().intValue(), 3);
        assertTrue(callBarringData.getNotificationToCSE());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(callBarringData.getExtensionContainer()));

        List<ExtCallBarringFeatureImpl> extCallBarringFeatures = callBarringData.getCallBarringFeatureList();
        assertNotNull(extCallBarringFeatures);
        assertEquals(extCallBarringFeatures.size(), 1);

        ExtCallBarringFeatureImpl extCallBarringFeature = extCallBarringFeatures.get(0);
        ExtBasicServiceCodeImpl extBasicServiceCode = extCallBarringFeature.getBasicService();
        ExtSSStatusImpl extSSStatus = extCallBarringFeature.getSsStatus();
        assertEquals(extBasicServiceCode.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allBearerServices);
        assertTrue(extSSStatus.getBitQ());
        assertFalse(extSSStatus.getBitP());
        assertFalse(extSSStatus.getBitR());
        assertFalse(extSSStatus.getBitA());

        PasswordImpl password = callBarringData.getPassword();
        assertNotNull(password);
        assertEquals(password.getData(), "0000");
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringDataImpl.class);
    	        
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allBearerServices));
        final ExtCallBarringFeatureImpl extCallBarringFeature = new ExtCallBarringFeatureImpl(extBasicServiceCode,
                new ExtSSStatusImpl(true, false, false, false), null);
        
        ArrayList<ExtCallBarringFeatureImpl> extCallBarringFeatureList=new ArrayList<ExtCallBarringFeatureImpl>();
        extCallBarringFeatureList.add(extCallBarringFeature);
        CallBarringDataImpl callBarringData = new CallBarringDataImpl(extCallBarringFeatureList,
                new PasswordImpl("0000"), 3, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        ByteBuf buffer=parser.encode(callBarringData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
