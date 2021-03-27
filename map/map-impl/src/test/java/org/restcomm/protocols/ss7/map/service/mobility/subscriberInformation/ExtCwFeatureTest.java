package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

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
public class ExtCwFeatureTest {
    private byte[] data = {48, 8, -95, 3, -125, 1, 32, -126, 1, 12};

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtCwFeatureImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtCwFeatureImpl);
        ExtCwFeatureImpl extCwFeature = (ExtCwFeatureImpl)result.getResult();

        ExtBasicServiceCodeImpl extBasicServiceCode = extCwFeature.getBasicService();
        assertEquals(extBasicServiceCode.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);

        ExtSSStatusImpl extSSStatus = extCwFeature.getSsStatus();
        assertTrue(extSSStatus.getBitQ());
        assertTrue(extSSStatus.getBitP());
        assertFalse(extSSStatus.getBitR());
        assertFalse(extSSStatus.getBitA());
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtCwFeatureImpl.class);
    	        
    	ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allShortMessageServices));
        ExtCwFeatureImpl extCwFeature = new ExtCwFeatureImpl(extBasicServiceCode, new ExtSSStatusImpl(true, true, false, false));

        ByteBuf buffer=parser.encode(extCwFeature);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
