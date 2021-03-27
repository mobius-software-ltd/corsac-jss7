package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class EctDataTest {
    private byte[] data = {48, 5, -127, 1, 14, -126, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(EctDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EctDataImpl);
        EctDataImpl ectData = (EctDataImpl)result.getResult();

        assertNotNull(ectData.getSsStatus());
        assertTrue(ectData.getSsStatus().getBitQ());
        assertTrue(ectData.getSsStatus().getBitP());
        assertTrue(ectData.getSsStatus().getBitR());
        assertFalse(ectData.getSsStatus().getBitA());
        assertTrue(ectData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(EctDataImpl.class);
    	        
    	EctDataImpl ectData = new EctDataImpl(new ExtSSStatusImpl(true, true, true, false), true);

    	ByteBuf buffer=parser.encode(ectData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
