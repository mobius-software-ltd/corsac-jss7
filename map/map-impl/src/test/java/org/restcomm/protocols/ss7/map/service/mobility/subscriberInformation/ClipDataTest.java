package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class ClipDataTest {
    private byte[] data = {48, 8, -127, 1, 0, -126, 1, 0, -125, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ClipDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ClipDataImpl);
        ClipDataImpl clipData = (ClipDataImpl)result.getResult();

        assertNotNull(clipData.getSsStatus());
        assertFalse(clipData.getSsStatus().getBitA());
        assertFalse(clipData.getSsStatus().getBitP());
        assertFalse(clipData.getSsStatus().getBitR());
        assertFalse(clipData.getSsStatus().getBitQ());
        assertEquals(clipData.getOverrideCategory(), OverrideCategory.overrideEnabled);
        assertTrue(clipData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ClipDataImpl.class);
    	        
    	ClipDataImpl clipData = new ClipDataImpl(new ExtSSStatusImpl(false, false, false, false), OverrideCategory.overrideEnabled, true);

    	ByteBuf buffer=parser.encode(clipData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
