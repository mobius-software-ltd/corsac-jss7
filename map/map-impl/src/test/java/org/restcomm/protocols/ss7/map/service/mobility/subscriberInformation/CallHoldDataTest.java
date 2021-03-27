package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class CallHoldDataTest {
    private byte[] data = {48, 5, -127, 1, 9, -126, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallHoldDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallHoldDataImpl);
        CallHoldDataImpl callHoldData = (CallHoldDataImpl)result.getResult();

        assertNotNull(callHoldData.getSsStatus());
        assertTrue(callHoldData.getSsStatus().getBitQ());
        assertFalse(callHoldData.getSsStatus().getBitP());
        assertFalse(callHoldData.getSsStatus().getBitR());
        assertTrue(callHoldData.getSsStatus().getBitA());
        assertTrue(callHoldData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallHoldDataImpl.class);
    	        
    	ExtSSStatusImpl extSSStatus = new ExtSSStatusImpl(true, false, false, true);
        CallHoldDataImpl callHoldData = new CallHoldDataImpl(extSSStatus, true);

        ByteBuf buffer=parser.encode(callHoldData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
