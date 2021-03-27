package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class CallWaitingDataTest {
    private byte[] data = {48, 14, -95, 10, 48, 8, -95, 3, -125, 1, 0, -126, 1, 15, -126, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallWaitingDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallWaitingDataImpl);
        CallWaitingDataImpl callWaitingData = (CallWaitingDataImpl)result.getResult();

        assertNotNull(callWaitingData.getCwFeatureList());
        assertEquals(callWaitingData.getCwFeatureList().size(), 1);
        assertTrue(callWaitingData.getNotificationToCSE());

        ExtCwFeatureImpl extCwFeature = callWaitingData.getCwFeatureList().get(0);
        assertNotNull(extCwFeature.getSsStatus());
        assertTrue(extCwFeature.getSsStatus().getBitQ());
        assertTrue(extCwFeature.getSsStatus().getBitP());
        assertTrue(extCwFeature.getSsStatus().getBitR());
        assertTrue(extCwFeature.getSsStatus().getBitA());
        assertEquals(extCwFeature.getBasicService().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallWaitingDataImpl.class);
    	        
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices));
        final ExtCwFeatureImpl extCwFeature = new ExtCwFeatureImpl(extBasicServiceCode, new ExtSSStatusImpl(true, true, true, true));
        
        ArrayList<ExtCwFeatureImpl> extCwFeatureList=new ArrayList<ExtCwFeatureImpl>();
        extCwFeatureList.add(extCwFeature);
        CallWaitingDataImpl callWaitingData = new CallWaitingDataImpl(extCwFeatureList, true);

        ByteBuf buffer=parser.encode(callWaitingData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
