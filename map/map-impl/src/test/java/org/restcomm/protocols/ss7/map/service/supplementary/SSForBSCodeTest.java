/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BearerServiceCodeImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
*
*/
public class SSForBSCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 3, 4, 1, 18 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 8, 4, 1, 18, -126, 1, 35, -124, 0 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
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

    @Test(groups = { "functional.encode", "service.supplementary" })
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