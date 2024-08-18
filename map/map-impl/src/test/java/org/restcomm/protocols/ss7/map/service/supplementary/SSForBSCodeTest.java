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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BearerServiceCodeImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class SSForBSCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 3, 4, 1, 18 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 8, 4, 1, 18, -126, 1, 35, -124, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSForBSCodeImpl.class);
        
        byte[] rawData = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSForBSCodeImpl);
        SSForBSCodeImpl impl = (SSForBSCodeImpl)result.getResult();
        
        assertEquals(impl.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.clir);
        assertNull(impl.getBasicService());
        assertFalse(impl.getLongFtnSupported());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSForBSCodeImpl);
        impl = (SSForBSCodeImpl)result.getResult();

        assertEquals(impl.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.clir);
        assertEquals(impl.getBasicService().getBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_1200_75bps);
        assertTrue(impl.getLongFtnSupported());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSForBSCodeImpl.class);
                
    	SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.clir);

        SSForBSCodeImpl impl = new SSForBSCodeImpl(ssCode, null, false);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));


        BearerServiceCodeImpl bearerService = new BearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_1200_75bps);
        BasicServiceCodeImpl basicService = new BasicServiceCodeImpl(bearerService);

        impl = new SSForBSCodeImpl(ssCode, basicService, true);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}