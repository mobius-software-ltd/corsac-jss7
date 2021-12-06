package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 */
public class ClirDataTest {
    private byte[] data = {48, 8, -127, 1, 10, -126, 1, 0, -125, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ClirDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ClirDataImpl);
        ClirDataImpl clirData = (ClirDataImpl)result.getResult();

        assertNotNull(clirData.getSsStatus());
        assertTrue(clirData.getSsStatus().getBitQ());
        assertFalse(clirData.getSsStatus().getBitP());
        assertTrue(clirData.getSsStatus().getBitR());
        assertFalse(clirData.getSsStatus().getBitA());
        assertEquals(clirData.getCliRestrictionOption(), CliRestrictionOption.permanent);
        assertTrue(clirData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ClirDataImpl.class);
    	        
    	ClirDataImpl clirData = new ClirDataImpl(new ExtSSStatusImpl(true, false, true, false), CliRestrictionOption.permanent, true);

    	ByteBuf buffer=parser.encode(clirData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
