package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class CallForwardingDataTest {
    private byte[] data = {48, 73, 48, 22, 48, 20, -124, 1, 5, -123, 6, -111, -119, 103, 69, 35, -15, -121, 1, 5, -118, 4, -111, 33, 67, -11, 5, 0, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallForwardingDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallForwardingDataImpl);
        CallForwardingDataImpl callForwardingData = (CallForwardingDataImpl)result.getResult();

        assertNotNull(callForwardingData.getForwardingFeatureList());
        assertEquals(callForwardingData.getForwardingFeatureList().size(), 1);
        assertTrue(callForwardingData.getNotificationToCSE());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(callForwardingData.getExtensionContainer()));

        ExtForwFeatureImpl extForwFeature = callForwardingData.getForwardingFeatureList().get(0);
        ISDNAddressStringImpl forwardedToNumber = extForwFeature.getForwardedToNumber();
        ExtSSStatusImpl extSSStatus = extForwFeature.getSsStatus();
        FTNAddressStringImpl longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNull(extForwFeature.getBasicService());
        assertNull(extForwFeature.getForwardedToSubaddress());
        assertNull(extForwFeature.getForwardingOptions());
        assertNull(extForwFeature.getExtensionContainer());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 5);
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(forwardedToNumber.getAddress(), "987654321");
        assertFalse(extSSStatus.getBitQ());
        assertTrue(extSSStatus.getBitP());
        assertFalse(extSSStatus.getBitR());
        assertTrue(extSSStatus.getBitA());
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(longForwardedToNumber.getAddress(), "12345");
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallForwardingDataImpl.class);
    	        
        ISDNAddressStringImpl forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "987654321");
        FTNAddressStringImpl longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "12345");
        final ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(null, new ExtSSStatusImpl(false, true, false, true), forwardedToNumber, null,
                null, 5, null, longForwardedToNumber);
        
        ArrayList<ExtForwFeatureImpl> extForwFeatureList=new ArrayList<ExtForwFeatureImpl>();
        extForwFeatureList.add(extForwFeature);
        CallForwardingDataImpl callForwardingData = new CallForwardingDataImpl(extForwFeatureList, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        ByteBuf buffer=parser.encode(callForwardingData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
